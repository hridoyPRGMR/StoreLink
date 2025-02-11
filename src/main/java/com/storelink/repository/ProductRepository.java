package com.storelink.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storelink.model.Product;
import com.storelink.projection.ProductProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@Query("""
			    SELECT p.id AS id, p.name AS name, p.description AS description,
			           c AS category,
			           b AS brand,
			           pi AS images,
			           v as variation
			    FROM Product p
			    LEFT JOIN p.category c
			    LEFT JOIN p.brand b
			    LEFT JOIN p.images pi
			    LEFT JOIN p.variation v
			    WHERE (:categoryId IS NULL OR c.id = :categoryId)
			      AND (:brandId IS NULL OR b.id = :brandId)
			      AND (:productName IS NULL OR p.name ILIKE CONCAT('%', :productName, '%'))
			""")
	Page<ProductProjection> findAllProjected(@Param("categoryId") Integer categoryId, @Param("brandId") Integer brandId,
			@Param("productName") String productName, Pageable pageable);

	
	@Query("""
			SELECT p FROM Shop s JOIN s.products p
			WHERE s.id = :shopId
			AND (:productName IS NULL OR p.name ILIKE CONCAT('%', :productName, '%'))
			AND (:categoryId IS NULL OR p.category.id = :categoryId)
			AND (:brandId IS NULL OR p.brand.id = :brandId)
	""")
	Page<ProductProjection> findShopProduct(
            @Param("shopId") Long shopId,
            @Param("productName") String productName,
            @Param("categoryId") Integer categoryId,
            @Param("brandId") Integer brandId,
            Pageable pageable);

}
