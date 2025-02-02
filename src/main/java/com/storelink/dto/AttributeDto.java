package com.storelink.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeDto {

    @NotEmpty(message = "SKU is required.")
    private String sku;

    @NotEmpty(message = "Value is required.")
    private String value;

    @NotNull(message = "Price is required.")
    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private Double price;
}
