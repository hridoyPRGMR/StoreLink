package com.storelink.model;


public class Attribute {
    
    private String sku;
    private String value;
    private Double price;

    public Attribute(){}

    public Attribute(String sku,String value,Double price){
        this.value=value;
        this.sku=sku;
        this.price=price;
    }

    public String getSku() {
        return sku;
    }

    public String getValue() {
        return value;
    }

    public Double getPrice() {
        return price;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Attribute [sku=" + sku + ", value=" + value + ", price=" + price + "]";
    }

}
