package com.storelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Variation;

@Repository
public interface VariationRepository extends JpaRepository<Variation,Long>{
    
}
