 package com.storelink.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.storelink.model.User;

import jakarta.persistence.criteria.Join;

public class UserSpecification {
    
    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null || email.isEmpty() ? null : criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> hasUsername(String username){
        return (root, query, criteriaBuilder) -> 
            username == null || username.isEmpty() ? null : criteriaBuilder.like(root.get("username"),"%" + username + "%" );
    }

    public static Specification<User> hasPhone(String phone){
        return (root, query, criteriaBuilder) -> 
            phone == null || phone.isEmpty() ? null : criteriaBuilder.like(root.get("phone"),"%" + phone + "%" );
    }

    public static Specification<User> hasRole(String role){
        return (root, query, criteriaBuilder) -> {
            if(role == null || role.isEmpty())return null;

            Join<Object,Object> rolesJoin = root.join("roles");
            return criteriaBuilder.equal(rolesJoin.get("name"), role);
        };
    }

    public static Specification<User> hasSearchTerm(String searchTerm){
        return (root, query, criteriaBuilder) ->{
            if(searchTerm==null || searchTerm.isEmpty())return null;

            String likeSearchTerm = "%" + searchTerm.toLowerCase() + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),likeSearchTerm),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),likeSearchTerm)
            );
        };
    }
    
 }