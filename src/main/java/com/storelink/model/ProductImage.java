package com.storelink.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "product_images")
public class ProductImage{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="pi_seq")
    @SequenceGenerator(name="pi_seq",sequenceName="pi_sequence",allocationSize=1)
    private long id;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(name="image_url",nullable = false,unique = true)
    private String imageUrl;

    public ProductImage() {
    }

    public ProductImage(Product product, String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
}
