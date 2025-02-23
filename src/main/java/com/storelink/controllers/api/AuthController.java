package com.storelink.controllers.api;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storelink.apiresponse.ApiResponse;
import com.storelink.dto.AuthReq;
import com.storelink.dto.UserDto;
import com.storelink.exceptions.UserNotFoundException;
import com.storelink.helpers.TokenGenerator;
import com.storelink.services.EmailService;
import com.storelink.services.JWTService;
import com.storelink.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UserService userService;
	private final EmailService emailService;
	private final AuthenticationManager authmanager;
	private final JWTService jwtService;
	
	public AuthController(AuthenticationManager authmanager,JWTService jwtService,UserService userService,EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
		this.authmanager = authmanager;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Map<String,String>>> login(@Valid @RequestBody AuthReq req) {
	    try {
	        Authentication authentication = authmanager.authenticate(
	            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
	        );

	        if (authentication.isAuthenticated()) 
	        {
	        	System.out.println("User authenticated.");
	        	
	        	Map<String,String> token = new HashMap<>();
	        	token.put("JwtToken", jwtService.generateToken(req.getUsername()));
	        	
	            return ResponseEntity.ok(new ApiResponse<>(true,"Logged in successfully.",token));
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false,"Unauthorized. Please check your credentials",null));
	    } 
	    catch (BadCredentialsException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false,"Invalid username or password",null));
	    } catch (DisabledException e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false,"Your account has been disabled. Please contact support",null));
	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false,"Authentication failed. Please check your credentials",null));
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false,"An unexpected error occurred. Please try again later",null));
	    }
	}

	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto req){
		
		if(userService.emailExist(req.getEmail())) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Email already exist.",null));
		}
		
//		emailService.sendVerificationEmail(req.getEmail(), TokenGenerator.generateToken());
		userService.saveUser(req, "ROLE_USER");
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"User Registered successfully",null));
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyEmail(@RequestParam String token) {
	     userService.verifyUser(token);
	     return ResponseEntity.ok(new ApiResponse<>(true,"User verification successful",null));
	}

	
}
