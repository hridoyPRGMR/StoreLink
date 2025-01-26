package com.storelink.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.storelink.model.User;
import com.storelink.model.UserPrincipal;
import com.storelink.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	private UserRepository userRep;
	
	public MyUserDetailsService(UserRepository userRep) {
		this.userRep = userRep;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRep.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		System.out.println(user);

		return new UserPrincipal(user);
	}
	
}
