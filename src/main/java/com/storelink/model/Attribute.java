package com.storelink.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(nullable = false, length = 255)
    private String value;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "variation_id", nullable = false)
    private Variation variation;

    public Attribute() {}

    public Attribute(String sku, String value, Double price) {
        this.sku = sku;
        this.value = value;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Attribute [sku=" + sku + ", value=" + value + ", price=" + price + "]";
    }
}
