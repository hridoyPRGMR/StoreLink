package com.storelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
	
	Permission findByPermission(String permission);
	
}
