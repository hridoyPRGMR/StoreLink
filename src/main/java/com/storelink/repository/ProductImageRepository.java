package com.storelink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long>{

    void deleteByImageUrlIn(List<String> images);

        
}
