package com.storelink.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storelink.model.ShopProduct;

@Repository
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {

	ShopProduct findByShopIdAndAttributeId(long shopId, long attributeId);

	Set<ShopProduct> findByShopId(long shopId);
	
}
