package com.storelink.dto;

import jakarta.validation.constraints.Size;

public class AuthReq {
	
	@Size(min=3,max=50)
	private String username;
	
	@Size(min = 6, max = 255)
	private String password;

	public AuthReq(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
