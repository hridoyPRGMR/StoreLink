package com.storelink.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShopDto {
	
	@Size(min=3,max=250,message="Shop name must be between 3 to 250 characters.")
	private String name;
	
	@Size(max=100,message="Introduction should be less than 1000 characters")
	private String introduction;
	
	private Double latitude;
	
	private Double longitude;
	
	private AddressDto address;
	
	private MultipartFile profilePhoto;
	
	private MultipartFile coverPhoto;
	
}
