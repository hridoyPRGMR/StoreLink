package com.storelink.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDto {
	
	@Column(nullable=false,length=50,unique=true)
	@Size(max=50,message="Permission must not exceed 50 characters.")
	@NotBlank(message = "Permission cannot be empty.")
	private String permission;
	
	public PermissionDto() {
		super();
	}

	public PermissionDto(String permission) {
		super();
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "PermissionDto [permission=" + permission + "]";
	}
	
	
	
}
