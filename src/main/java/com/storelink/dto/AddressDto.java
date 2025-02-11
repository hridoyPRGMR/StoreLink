package com.storelink.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDto {

	@Size(max=250)
	private String street;
	
	@Size(max=250)
	private String city;
	
	@Size(max=250)
	private String state;
	
	@Size(max=250)
	private String country;
	
	@Size(max=250)
	private String postalCode;
	
	
	
}
