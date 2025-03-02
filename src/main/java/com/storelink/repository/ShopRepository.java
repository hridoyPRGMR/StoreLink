package com.storelink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storelink.model.Shop;
import com.storelink.projection.ShopProjection;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
	                                      
	
}
