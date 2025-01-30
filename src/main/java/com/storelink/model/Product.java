package com.storelink.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    private String name;
    private int categoryId;
    private int brandId;
    private String description;
    private List<String>images;

    private Variation variation;

    private long createdBy;
    private long updatedBy;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDate.now();
    }

}
