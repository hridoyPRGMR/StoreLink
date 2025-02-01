package com.storelink.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.storelink.dto.ProductDto;
import com.storelink.model.Product;
import com.storelink.repository.ProductRepository;

import jakarta.annotation.PostConstruct;


@Service
public class ProductService {

    private final ProductRepository productRep;
    private final CurrentUserService userService;
    private final ImageService imageServ;

    public ProductService(ProductRepository productRep,CurrentUserService userService,ImageService imageServ){
        this.productRep = productRep;
        this.userService = userService;
        this.imageServ=imageServ;
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

        List<String>imageNames = new ArrayList<>();
        List<MultipartFile> images = req.getImages();

        for(MultipartFile file:images){
            String fileName;
            try {
                //product is folder name here
                fileName = imageServ.saveImage(file,"product");
                imageNames.add(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image: " + file.getOriginalFilename(), e);
            }
            
        }

        product.setImages(imageNames);  
        return product;
    }

    


}
