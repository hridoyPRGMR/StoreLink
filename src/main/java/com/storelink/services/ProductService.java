package com.storelink.services;

import org.springframework.stereotype.Service;

import com.storelink.dto.ProductDto;
import com.storelink.model.Product;
import com.storelink.repository.ProductRepository;

import jakarta.validation.Valid;

@Service
public class ProductService {
    
    private final ProductRepository productRep;
    private final CurrentUserService userService;

    public ProductService(ProductRepository productRep,CurrentUserService userService){
        this.productRep = productRep;
        this.userService = userService;
    }

    public void saveProduct(ProductDto req) {
        Product product = toEntity(req);
        productRep.save(product);
    }


    public Product toEntity(ProductDto req){

        Product product = new Product();

        product.setName(req.getName());
        product.setBrandId(req.getBrandId());
        product.setCategoryId(req.getCategoryId());
        product.setDescription(req.getDescription());
        product.setVariation(req.getVariation());
        product.setCreatedBy(userService.getCurrentUserId());

        return product;
    }

}
