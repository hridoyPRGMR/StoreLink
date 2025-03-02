package com.storelink.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ShopProductDto {
	
	@NotNull
	@Min(value=1)
	private long attributeId;
	
	private int stock;
	
}
