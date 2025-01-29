package com.storelink.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    
    @NotNull(message = "Category name can not be null.")
    @Size(min=1,max = 150,message = "Category name must be between 1 and 150 characters.")
    private String name;    

    @Size(max=300,message = "Category description can not be exceed 300 character.")
    private String description;

}
