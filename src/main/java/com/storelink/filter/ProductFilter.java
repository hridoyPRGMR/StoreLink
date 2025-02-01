package com.storelink.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ProductFilter {
    
    public static Query buildQuery(Integer categoryId,Integer brandId,String productName){

        Query query = new Query();

        if(categoryId != null){
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }

        if(brandId != null){
            query.addCriteria(Criteria.where("brandId").is(brandId));
        }

        if(productName != null && !productName.isEmpty()){
            query.addCriteria(Criteria.where("productName").regex(productName,"i"));
        }

        return query;
    }
    
}
