package com.storelink.repository;



import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Long>{

    void deleteByVariationId(Long variationId);
    
    @EntityGraph(attributePaths = {"variation","variation.product"})
    List<Attribute> findAllByIdIn(List<Long>ids);
    
}
