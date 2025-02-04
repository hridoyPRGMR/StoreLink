package com.storelink.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.storelink.dto.AttributeDto;
import com.storelink.dto.ProductDto;
import com.storelink.dto.VariationDto;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Attribute;
import com.storelink.model.Product;
import com.storelink.model.ProductImage;
import com.storelink.model.Variation;
import com.storelink.repository.AttributeRepository;
import com.storelink.repository.ProductImageRepository;
import com.storelink.repository.ProductRepository;
import com.storelink.repository.VariationRepository;

import com.storelink.specifications.ProductSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final VariationRepository variationRep;
    private final ProductRepository productRep;
    private final CurrentUserService userServ;
    private final ImageService imageServ;   
    private final BrandService brandServ;
    private final CategoryService categoryServ;
    private final ProductImageRepository productImgRep;
    private final AttributeRepository attributeRep;

    @Transactional
    public void saveProduct(ProductDto req) {
        
        Variation variation = variationDtoToEntity(req.getVariation());
        variation = variationRep.save(variation);

        Product  product = toEntity(req);
        product.setVariation(variation); 

        productRep.save(product);
    }

    public Product findById(long id){
        return productRep.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+id));
    }

    public Page<Product> getAllPaginatedProducts(Integer categoryId, Integer brandId, String productName, int page,int size){
           
        PageRequest pageable = PageRequest.of(page,size);

        Specification<Product> specification = ProductSpecification.filterProducts(categoryId, brandId, productName);

        return productRep.findAll(specification,pageable);
    }


    @Transactional
    public void updateProduct(ProductDto req, long productId) {
        
        // Fetch existing product and variation
        Product product = productRep.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Variation variation = product.getVariation();

        // Update variation data
        VariationDto variationDto = req.getVariation();
        
        variation.setAttributeType(variationDto.getAttributeType());
        variation.setUnit(variationDto.getUnit());

        // Convert and set attributes
        List<Attribute> newAttributes = variationDto.getAttributes()
                .stream()
                .filter(attDto -> attDto.getSku() != null && attDto.getValue() != null && attDto.getPrice()!=null)
                .map(attDto -> {
                    Attribute attribute = attributeDtoToEntity(attDto);
                    attribute.setVariation(variation);
                    return attribute;
                })
                .collect(Collectors.toList());
        
        
        attributeRep.deleteByVariationId(variation.getId());
        attributeRep.flush();

        variation.getAttributes().clear();
        variation.getAttributes().addAll(newAttributes);

        List<ProductImage> newImages = toProductImages(req.getImages(), product);
        List<ProductImage> images = product.getImages();
        for(ProductImage image:newImages){
            images.add(image);
        }

        // Update product details
        product.setName(req.getName());
        product.setCategory(categoryServ.findById(req.getCategoryId()));
        product.setBrand(brandServ.findByBrandId(req.getBrandId()));
        product.setDescription(req.getDescription());
        product.setVariation(variation);
        product.setImages(images);

        // Save the updated product
        productRep.save(product);
    }

    @Transactional
    public void deleteById(long id) throws IOException {
        Product product = findById(id);
        
        List<String> imageNames = product.getImages()
            .stream()
            .map(ProductImage::getImageUrl)
            .collect(Collectors.toList());
        
        deleteImages(imageNames, "product");

        productRep.delete(product);
    }

    
    @Transactional
    public void deleteImages(List<String> images,String folder) throws IOException{
        productImgRep.deleteByImageUrlIn(images);
        imageServ.deleteImages(images, folder);
    }


    //mapper methods
    public Product toEntity(ProductDto req) {
        
        Product product = new Product();
        
        product.setName(req.getName());
        product.setBrand(brandServ.findByBrandId(req.getBrandId()));
        product.setCategory(categoryServ.findById(req.getCategoryId()));
        product.setDescription(req.getDescription());
        product.setCreatedBy(userServ.getCurrentUserId());

        product.setImages(toProductImages(req.getImages(),product));

        return product;
    }

    public Variation variationDtoToEntity(VariationDto dto) {
        Variation variation = new Variation();
        variation.setAttributeType(dto.getAttributeType());
        variation.setUnit(dto.getUnit());
    
        List<AttributeDto> attributeDtos = dto.getAttributes();
        List<Attribute> attributes = new ArrayList<>();
    
        for (AttributeDto attDto : attributeDtos) {
            Attribute attribute = attributeDtoToEntity(attDto);
            attribute.setVariation(variation);
            attributes.add(attribute);
        }
    
        variation.setAttributes(attributes); 
        return variation;
    }

    private Attribute attributeDtoToEntity(AttributeDto dto) {
        Attribute attribute = new Attribute();

        attribute.setPrice(dto.getPrice());
        attribute.setSku(dto.getSku());
        attribute.setValue(dto.getValue());

        return attribute;
    }


    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        
        dto.setName(product.getName());
        dto.setBrandId(product.getBrand().getId()); 
        dto.setCategoryId(product.getCategory().getId());
        dto.setDescription(product.getDescription());
        
        dto.setImages(new ArrayList<>());
        
        if (product.getVariation() != null) {
            dto.setVariation(variationEntityToDto(product.getVariation()));
        }
    
        return dto;
    }
    

    public VariationDto variationEntityToDto(Variation variation) {
        VariationDto dto = new VariationDto();
        dto.setAttributeType(variation.getAttributeType());
        dto.setUnit(variation.getUnit());
    
        List<AttributeDto> attributeDtos = new ArrayList<>();
        for (Attribute attribute : variation.getAttributes()) {
            attributeDtos.add(attributeEntityToDto(attribute));
        }
    
        dto.setAttributes(attributeDtos);
        return dto;
    }
    
    private AttributeDto attributeEntityToDto(Attribute attribute) {
        AttributeDto dto = new AttributeDto();
        
        dto.setPrice(attribute.getPrice());
        dto.setSku(attribute.getSku());
        dto.setValue(attribute.getValue());
    
        return dto;
    }

    private List<ProductImage> toProductImages(List<MultipartFile> files,Product product)
        {
            List<ProductImage> images = new ArrayList<>();
            
            for (MultipartFile file : files) {
                try {
                    if(!file.isEmpty() && file != null){
                        String imageName = imageServ.saveImage(file, "product");
                        images.add(new ProductImage(product,imageName));
                    }
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image: " + file.getOriginalFilename(), e);
            }
        }
        return images;
    }

}
