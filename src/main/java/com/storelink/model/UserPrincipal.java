package com.storelink.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public UserPrincipal(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		authorities.addAll(user.getRoles()
				.stream()
				.map(role-> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList()));
		
		if(user.getRoles().stream().anyMatch(role-> role.getName().equals("ROLE_ADMIN"))) {
			authorities.addAll(user.getPermissions().stream()
				.map(permission-> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList())
			);
		}
		
		System.out.println(authorities);

		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

}
