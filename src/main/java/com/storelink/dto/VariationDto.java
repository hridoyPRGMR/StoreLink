package com.storelink.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VariationDto {

    @NotEmpty(message = "Attribute type is required.")
    private String attributeType;

    @NotEmpty(message = "Unit is required.")
    private String unit;

    @NotEmpty(message = "At least one attribute is required.")
    private List<AttributeDto> attributes;
}
