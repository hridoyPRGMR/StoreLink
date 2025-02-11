package com.storelink.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.storelink.model.User;
import com.storelink.model.UserPrincipal;

@Service
public class CurrentUserService {

    public UserPrincipal getCurrentUser(){
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal){
            return (UserPrincipal) authentication.getPrincipal();
        }

        throw new IllegalStateException("No authenticated user found.");
    }

    public String getCurrentUsername(){
        return getCurrentUser().getUsername();
    }

    public Long getCurrentUserId(){
        return getCurrentUser().getUser().getId();
    }
    
    public User getUser() {
    	return getCurrentUser().getUser();    
    }

}
