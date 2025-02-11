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
@Table(name="addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="address_seq")
    @SequenceGenerator(name="address_seq",sequenceName="address_sequence",allocationSize=1)
	private long id;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String postalCode;
	
}
