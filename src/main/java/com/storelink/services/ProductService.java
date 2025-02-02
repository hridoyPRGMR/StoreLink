package com.storelink.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.storelink.model.Variation;
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

    @Transactional
    public void saveProduct(ProductDto req) {
        
        Variation variation = variationDtoToEntity(req.getVariation());
        variation = variationRep.save(variation);

        Product  product = toEntity(req);
        product.setVariation(variation); 
        
        productRep.save(product);
    }

    public Product findById(int id){
        return productRep.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+id));
    }

    public Page<Product> getAllPaginatedProducts(Integer categoryId, Integer brandId, String productName, int page,int size){
           
        PageRequest pageable = PageRequest.of(page,size);

        Specification<Product> specification = ProductSpecification.filterProducts(categoryId, brandId, productName);

        return productRep.findAll(specification,pageable);
    }

    public Product toEntity(ProductDto req) {
        Product product = new Product();
        
        product.setName(req.getName());
        product.setBrand(brandServ.findByBrandId(req.getBrandId()));
        product.setCategory(categoryServ.findById(req.getCategoryId()));
        product.setDescription(req.getDescription());
        product.setCreatedBy(userServ.getCurrentUserId());
    
        List<String> imageNames = new ArrayList<>();
        for (MultipartFile file : req.getImages()) {
            try {
                imageNames.add(imageServ.saveImage(file, "product"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image: " + file.getOriginalFilename(), e);
            }
        }
        product.setImages(imageNames);
    
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

    
}
