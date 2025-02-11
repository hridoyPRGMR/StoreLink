package com.storelink.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="shop")
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_seq")
	@SequenceGenerator(name="user_seq",sequenceName="user_sequence",allocationSize=1)
	private Long id;
	
	@Column(nullable=false,unique=true,length=100)
	private String email;
	
	@Column(nullable=false,length=100)
	private String name;
	
	private String phone;
	
	@Column(unique=true,nullable=false,length=100)
	private String username;
	

	@Column(nullable = false,length=100)
	private String password;

	private boolean verified = false;
	
	@Column(length=250)
	private String verificationToken;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
	
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    	name = "admin_permissions",
    	joinColumns = @JoinColumn(name = "user_id"),
    	inverseJoinColumns = @JoinColumn(name="permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
    
    @JsonManagedReference
    @OneToOne(mappedBy="user",fetch=FetchType.LAZY)
    private Shop shop;
    
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
