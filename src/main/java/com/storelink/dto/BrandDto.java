package com.storelink.dto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    
    @Size(min=1,max=150,message="Brand name must be in between 1 to 150 characters.")
    private String name;

    @Size(max = 300,message = "Brand description must be less than or equal 300 characters.")
    private String description;
    
}
