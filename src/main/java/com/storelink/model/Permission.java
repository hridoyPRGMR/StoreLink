package com.storelink.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="permissions")
public class Permission {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "per_seq")
	@SequenceGenerator(name="per_seq",sequenceName="permission_sequence",allocationSize=1)
    private Long id;
	
    private String permission;
	
}
