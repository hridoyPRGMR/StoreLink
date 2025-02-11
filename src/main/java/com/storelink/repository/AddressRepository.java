package com.storelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
