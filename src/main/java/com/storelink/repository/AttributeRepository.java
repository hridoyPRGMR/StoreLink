package com.storelink.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Long>{

    void deleteByVariationId(Long variationId);
    
}
