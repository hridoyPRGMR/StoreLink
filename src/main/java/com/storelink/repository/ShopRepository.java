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

	@Query(value = """
			    SELECT DISTINCT s.id AS shopId, s.name AS shopName, s.latitude, s.longitude,
			           ST_Distance(s.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography) AS distance,
			           a.id AS addressId, a.street, a.city, a.state, a.country, a.postal_code
			    FROM shop s
			    JOIN shop_products sp ON s.id = sp.shop_id
			    JOIN addresses a ON s.address_id = a.id
			    WHERE sp.product_id = :productId
			      AND sp.stock > 0
			    ORDER BY distance
			    LIMIT 5
			""", nativeQuery = true)
	List<ShopProjection> findNearestShopWithProduct(@Param("latitude") Double latitude,
			@Param("longitude") Double longitude, @Param("productId") Long productId);

}
