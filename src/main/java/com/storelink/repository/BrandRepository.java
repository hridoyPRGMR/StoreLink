package com.storelink.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {

    boolean existsByName(String name);
    
    Page<Brand> findByNameContainingIgnoreCase(String name,Pageable pageable);

    boolean existsByNameAndIdNot(String name, int id);
}
