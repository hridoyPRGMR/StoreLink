package com.storelink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	@NotBlank(message = "Email required.")
	private String email;
	
	@Size(min=1,max=100)
	private String name;
	
	@Size(max=30)
	private String phone;

	@Size(min=3,max = 50)
	private String username;
	
	@Size(min = 6, max = 255)
	private String password;
	
	
}
