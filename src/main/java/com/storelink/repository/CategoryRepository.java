package com.storelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{
    
}
