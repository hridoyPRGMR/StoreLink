package com.storelink.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDto {

    @Size(min = 1, max = 250, message = "Product name must be between 1 to 250 characters.")
    private String name;

    @NotNull(message = "Category is required.")
    @Min(value = 1, message = "Invalid category.")
    private Integer categoryId;

    @NotNull(message = "Brand is required.")
    @Min(value = 1, message = "Invalid brand.")
    private Integer brandId;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters.")
    private String description;

    @NotNull(message = "Variation is required.")
    private VariationDto variation;

    @NotEmpty(message = "At least one image is required.")
    private List<MultipartFile> images;
}
