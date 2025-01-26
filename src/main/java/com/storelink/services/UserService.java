package com.storelink.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.storelink.dto.UserDto;
import com.storelink.model.Permission;
import com.storelink.model.User;
import com.storelink.repository.PermissionRepository;
import com.storelink.repository.UserRepository;
import com.storelink.specifications.UserSpecification;

@Service
public class UserService {

    private UserRepository userRep;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleService roleServ;
    private PermissionRepository permRep;


    public UserService(UserRepository userRep,RoleService roleServ,PermissionRepository permRep){
        this.roleServ=roleServ;
        this.userRep = userRep;
        this.permRep = permRep;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    public User saveUser(UserDto userDto,String role){

        User user =  toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(roleServ.findByName(role)));

        return userRep.save(user);
    }

    public User toEntity(UserDto userDto){

        User user = new User();
        
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setUsername(userDto.getUsername());

        return user;
    }

    public Page<User> getUsers(String searchTerm,String role,int page,int size) {

        PageRequest pageable = PageRequest.of(page, size);

        Specification<User> specification = Specification.where(UserSpecification.hasRole(role))
            .and(UserSpecification.hasSearchTerm(searchTerm));

        return userRep.findAll(specification, pageable);
    }

    public void assignPermission(Long userId,List<Long> permissionIds){

        User user = userRep.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found!! with Id: "+ userId));
        List<Permission> permissions = permRep.findAllById(permissionIds);     

        user.setPermissions(new HashSet<>(permissions));

        userRep.save(user);
    }

}
