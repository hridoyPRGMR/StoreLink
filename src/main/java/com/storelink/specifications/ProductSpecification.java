package com.storelink.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.storelink.model.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
    

    public static Specification<Product> filterProducts(Integer categoryId,Integer brandId,String name){

        return (root,query,criteriaBuilder) -> {
            
            List<Predicate> predicates = new ArrayList<>();

            if(name!=null && !name.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasName(String name){
        return (root,query,criteriaBuilder)-> 
            name==null || name.isEmpty() ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

}
