package com.storelink.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Variation {

    private String variationId;
    private String attributeType;
    private String unit;
    private List<Attribute>attributes;

}
