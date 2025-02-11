package com.storelink.projection;

import java.util.List;

import com.storelink.model.Variation;

public interface ProductProjection {
    
    Long getId();
    
    String getName();
    
    String getDescription();
    
    CategoryProjection getCategory();
    
    BrandProjection getBrand();
    
    List<ProductImageProjection> getImages();
    
    Variation getVariation();
    
    interface CategoryProjection {
        Integer getId();
        String getName();
    }
    
    interface BrandProjection {
        Integer getId();
        String getName();
    }
    
    interface ProductImageProjection {
        Long getId();
        String getImageUrl();
    }
    
}
