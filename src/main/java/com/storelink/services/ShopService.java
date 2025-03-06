package com.storelink.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.storelink.dto.AddressDto;
import com.storelink.dto.ShopDto;
import com.storelink.dto.ShopProductDto;
import com.storelink.exceptions.ConflictException;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Address;
import com.storelink.model.Attribute;
import com.storelink.model.Product;
import com.storelink.model.Shop;
import com.storelink.model.ShopProduct;
import com.storelink.model.User;
import com.storelink.model.Variation;
import com.storelink.projection.ProductProjection;
import com.storelink.projection.ShopProjection;
import com.storelink.repository.AddressRepository;
import com.storelink.repository.AttributeRepository;
import com.storelink.repository.ShopProductRepository;
import com.storelink.repository.ShopRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ShopService {

    private final AddressRepository addressRep;
    private final ShopRepository shopRep;
    private final UserService userServ;
    private final ProductService productServ;
    private final ShopProductRepository shopProdRep;
    private final AttributeRepository attributeRep;

    
    public Shop findById(Long id) {
    	return shopRep.findById(id).orElseThrow(()-> new ResourceNotFoundException("Shop not found with ID: "+id));
    }

    @Transactional
    public Shop saveShop(ShopDto req, String username) {
        Address address = addressRep.save(toEntity(req.getAddress()));
        Shop shop = toEntity(req);
        
        User user = userServ.findByUsername(username);
        if(user.getShop() != null ) {
        	throw new ConflictException("Shop already exist for the user.");
        }
        
        shop.setUser(user);
        shop.setAddress(address);

        return shopRep.save(shop);
    }
    
    public void addProducts(List<ShopProductDto> req,String username) {
		
    	Shop shop = userServ.findByUsername(username).getShop();
    	
    	if(shop == null) {
    		throw new ResourceNotFoundException("Store not exist for the user");
    	}
    	
    	// extract attribute IDs from request
    	List<Long> attributeIds = req.stream().map(ShopProductDto::getAttributeId).collect(Collectors.toList());
    	// fetch attributes from DB
    	List<Attribute> attributes = attributeRep.findAllById(attributeIds);
    	
    	
//    	Map attribute IDs to their corresponding Attribute objects
    	Map<Long,Attribute> attributeMap = attributes.stream().collect(Collectors.toMap(Attribute::getId, Function.identity()));
    	
    	Set<Long> existingAttributeIds  = shopProdRep.findByShopId(shop.getId())
    		.stream()
    		.map(ShopProduct::getAttribute)
    		.map(Attribute::getId)
    		.collect(Collectors.toSet());
    	
    	List<ShopProduct> shopProducts = new ArrayList<>();
    	Set<Long> seenAttributes = new HashSet<>(); // To filter out duplicates in the request
    	
    	for (ShopProductDto spd : req) {
            Long attrId = spd.getAttributeId();

            // skip if attribute already exists in DB or if it's duplicated in request
            if (existingAttributeIds.contains(attrId) || !seenAttributes.add(attrId)) {
                continue;
            }

            Attribute attribute = attributeMap.get(attrId);
            if (attribute == null) {
                throw new ResourceNotFoundException("Attribute not found for id: " + attrId);
            }

            Product product = Optional.ofNullable(attribute.getVariation())
                    .map(Variation::getProduct)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for attribute id: " + attrId));

            ShopProduct shopProduct = new ShopProduct(shop, product, attribute, spd.getStock());
            shopProducts.add(shopProduct);
        }
    		
    	shopProdRep.saveAll(shopProducts);
	}

    public Shop toEntity(ShopDto req) {
        Shop shop = new Shop();
        shop.setName(req.getName());
        shop.setIntroduction(req.getIntroduction());
        shop.setLatitude(req.getLatitude());
        shop.setLongitude(req.getLongitude());

        Point point = convertToPoint(req.getLatitude(), req.getLongitude());
        shop.setLocation(point);

        return shop;
    }

    public Address toEntity(AddressDto req) {
        Address address = new Address();
        address.setCity(req.getCity());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
        address.setState(req.getState());
        address.setStreet(req.getStreet());
        return address;
    }

    public Point convertToPoint(Double lat, Double lng) {
        if (!isValidLatitude(lat) || !isValidLongitude(lng)) {
            throw new IllegalArgumentException("Invalid latitude or longitude");
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
        
        return point;
    }
    

    public boolean isValidLatitude(Double lat) {
        return lat >= -90 && lat <= 90;
    }

    public boolean isValidLongitude(Double lng) {
        return lng >= -180 && lng <= 180;
    }

	public Page<ProductProjection> getProducts(String productName, Integer categoryId, Integer brandId, int page, int size) {
		return productServ.getAllPaginatedProductsForApi(categoryId, brandId, productName, page, size);
	}

	public Page<ProductProjection> getShopProducts(String productName, Integer categoryId, Integer brandId, int page,
			int size, String username) {
		
		Shop shop =  userServ.findByUsername(username).getShop();
		
		if(shop!=null) {
			Long shopId = shop.getId();
			Page<ProductProjection> shopProduct = productServ.getShopProduct(shopId, categoryId, brandId, productName, page, size);
	
			
			List<Long> shopAttributeIds = shop.getProducts().stream()
		    	.map(sp -> sp.getAttribute().getId())
		    	.collect(Collectors.toList());
			
			shopProduct.forEach(product->{
				List<Attribute> attributes = product.getVariation().getAttributes().stream()
					.filter(attribute -> shopAttributeIds.contains(attribute.getId()))
					.collect(Collectors.toList());
				
				product.getVariation().setAttributes(attributes);
			});
			
			return shopProduct;
		}
		else {
			throw new ResourceNotFoundException("User did not have Shop.");
		}
	}


	public void removeProduct(List<Long> productIds,String username) {
		
		User user = userServ.findByUsername(username);
		Shop shop = user.getShop(); 
		
		List<Product> products = productServ.getProducts(productIds);
		shop.getProducts().removeAll(products);
		
		shopRep.save(shop);
	}

	public List<ShopProjection> findNearestShop(Double latitude, Double longitude, Long productId) {
//		
//		List<ShopProjection> shops = shopRep.findNearestShopWithProduct(
//		        latitude, 
//		        longitude,
//		        productId
//		    );
		
		List<ShopProjection> shops = new ArrayList<>();
		
		System.out.println(shops);
		
		return shops;
	}

	public void updateProduct(long attributeId,int stock, String username) {
		
		long shopId = userServ.findByUsername(username).getShop().getId();
		
		ShopProduct shopProduct = shopProdRep.findByShopIdAndAttributeId(shopId,attributeId);
		shopProduct.setStock(stock);
		
		shopProdRep.save(shopProduct);
	}
	
}
