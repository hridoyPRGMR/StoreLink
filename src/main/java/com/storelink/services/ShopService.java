package com.storelink.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.storelink.dto.AddressDto;
import com.storelink.dto.ShopDto;
import com.storelink.exceptions.ConflictException;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Address;
import com.storelink.model.Product;
import com.storelink.model.Shop;
import com.storelink.model.User;
import com.storelink.projection.ProductProjection;
import com.storelink.projection.ShopProjection;
import com.storelink.repository.AddressRepository;
import com.storelink.repository.ShopRepository;

import jakarta.transaction.Transactional;

@Service
public class ShopService {

    private final AddressRepository addressRep;
    private final ShopRepository shopRep;
    private final UserService userServ;
    private final ProductService productServ;

    public ShopService(ShopRepository shopRep, AddressRepository addressRep, UserService userServ,ProductService productServ) {
        this.shopRep = shopRep;
        this.userServ = userServ;
        this.addressRep = addressRep;
        this.productServ = productServ;
    }
    
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
    
    public void addProducts(List<Long> productIds,String username) {
		
    	User user = userServ.findByUsername(username);
    	Shop shop = user.getShop();
    	
    	if(shop == null) {
    		throw new ResourceNotFoundException("Store not exist for the user");
    	}
    	
		List<Product> products = productServ.getProducts(productIds);
		
		Set<Product> setOfproduct =  products.stream()
				.collect(Collectors.toSet());
		
		shop.setProducts(setOfproduct);
		
		shopRep.save(shop);
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
		
		User user = userServ.findByUsername(username);
		Shop shop = user.getShop();
		
		if(shop!=null) {
			Long shopId = shop.getId();
			return productServ.getShopProduct(shopId, categoryId, brandId, productName, page, size);
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
		
		List<ShopProjection> shops = shopRep.findNearestShopWithProduct(
		        latitude, 
		        longitude,
		        productId
		    );
		
		System.out.println(shops);
		
		return shops;
	}

	
}
