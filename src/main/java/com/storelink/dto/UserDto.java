package com.storelink.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message = "Email required.")
	private String email;
	
	@Size(min=1,max=100)
	private String name;
	
	private String phone;

	@Size(min=3,max = 50)
	private String username;
	
	@Size(min = 6, max = 255)
	private String password;
	
}
