package com.storelink.dto;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BrandDto {
    
    @Size(min=1,max=150,message="Brand name must be in between 1 to 150 characters.")
    private String name;

    @Size(max = 300,message = "Brand description must be less than or equal 300 characters.")
    private String description;
    
}
