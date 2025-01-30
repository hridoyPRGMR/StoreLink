package com.storelink.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.storelink.model.Variation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDto {
    
    @Size(min=1, max = 250,message = "Product name must be between 1 to 250 characters")
    private String name;

    @NotNull(message="Category required.")
    @Min(value = 1,message = "Invalid Category.")
    private int categoryId;

    @NotNull(message = "Brand required.")
    @Min(value = 1,message = "Invalid Brand")
    private int brandId;

    @Size(max=500,message = "Description must be less than or equal to 500 characters")
    private String description;

    @NotNull(message = "Variation required.")
    private Variation variation;

    private List<MultipartFile> images;

}
