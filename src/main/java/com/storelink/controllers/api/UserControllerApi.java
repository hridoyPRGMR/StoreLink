package com.storelink.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storelink.repository.UserRepository;
import com.storelink.services.JWTService;

@RestController
@RequestMapping("/api/auth")
public class UserControllerApi {
	
	@Autowired
	AuthenticationManager authmanager;
	
	private UserRepository userRep;
	

	@Autowired
	private  JWTService jwtService;
	
	public UserControllerApi(UserRepository userRep) {
		this.userRep=userRep;
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthReq req){
		
//		User user=userRep.findByUsername(req.getUsername());
		
		Authentication authentication = authmanager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword()));
		
		if(authentication.isAuthenticated()) {
			return ResponseEntity.ok(jwtService.generateToken(req.getUsername()));
		}
		
		return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
	}
	
	
}


class AuthReq{
	
	private String username;
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