package com.storelink.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
    
}
