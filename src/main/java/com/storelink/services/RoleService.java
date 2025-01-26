package com.storelink.services;

import org.springframework.stereotype.Service;

import com.storelink.model.Role;
import com.storelink.repository.RoleRepository;

@Service
public class RoleService {

    private RoleRepository roleRep;

    public RoleService(RoleRepository roleRep){
        this.roleRep = roleRep;
    }
    
    public Role findByName(String name){
        return roleRep.findByName(name);
    }

}