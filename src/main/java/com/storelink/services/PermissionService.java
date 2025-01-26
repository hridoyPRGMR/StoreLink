package com.storelink.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.storelink.dto.PermissionDto;
import com.storelink.model.Permission;
import com.storelink.repository.PermissionRepository;

@Service
public class PermissionService {

	private PermissionRepository perRep;
	
	public PermissionService(PermissionRepository perRep) {
		this.perRep=perRep;
	}
	
	public Permission save(Permission permission) {
		return perRep.save(permission);
	}
	
	public Optional<Permission> findById(Long id) {
		return perRep.findById(id);
	}
	
	public Permission findByPermission(String permission) {
		return perRep.findByPermission(permission);
	}
	
	public List<Permission> getAllPermission(){
		return perRep.findAll();
	}
	
	public void deleteById(Long id) {
		perRep.deleteById(id);
	}
	
	public Permission convertToEntity(PermissionDto permissionDto) {
		Permission permission = new Permission();
		permission.setPermission(permissionDto.getPermission());
		return permission;
	}
	
	public PermissionDto convertToDtO(Permission permission) {
		
		PermissionDto dto = new PermissionDto();
		dto.setPermission(permission.getPermission());
		return dto;
	}
	
}
