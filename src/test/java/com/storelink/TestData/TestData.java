package com.storelink.TestData;

import java.util.Set;

import com.storelink.model.Address;
import com.storelink.model.Role;
import com.storelink.model.Shop;
import com.storelink.model.User;

public class TestData {
	
	public static User createUser() {
		
		User user = new User();
		user.setId(1L);
		user.setName("TestUser");
		user.setUsername("testuser");
		user.setPassword("password");
		user.setEmail("testuser@gmail.com");
		user.setRoles(Set.of(createRole()));
		
		return user;
	}
	
	public static Role createRole() {
		Role role = new Role();
		role.setId(1L);
		role.setName("ROLE_USER");
		return role;
	}
	
	public static Shop createShop() {
		
		Shop shop = new Shop();
		shop.setId(1L);
		shop.setName("TestShop");
		shop.setIntroduction("This Shop is for test.");
		shop.setLongitude(23999.00);
		shop.setLongitude(90000.00);
		
		return shop;
	}
	
	public static Address createAddress() 
	{	
		Address address = new Address(); 
		address.setId(1L);
		address.setStreet("street");
		address.setCity("city");
		address.setCountry("country");
		address.setPostalCode("1980");
		address.setState("state");
		return address;
	}
}
