package com.storelink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.storelink.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
	
	User findByUsername(String username);

	Optional<User> findByVerificationToken(String token);
	
	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}
