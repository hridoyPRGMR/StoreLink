package com.storelink.controllers.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storelink.api.response.ApiResponse;
import com.storelink.dto.ShopDto;
import com.storelink.model.Shop;
import com.storelink.projection.ProductProjection;
import com.storelink.projection.ShopProjection;
import com.storelink.services.ShopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

	private final ShopService shopServ;

	public ShopController(ShopService shopServ) {
		this.shopServ = shopServ;
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Shop>> createShop(@Valid @RequestBody ShopDto req,
			Authentication authentication) {

		String username = authentication.getName();
		Shop shop = shopServ.saveShop(req, username);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Shop created successfully", shop));

	}

	@PostMapping("/add-products/{shopId}")
	public ResponseEntity<?> addProducts(@RequestBody List<Long> productIds, @PathVariable Long shopId) {
		shopServ.addProducts(productIds, shopId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Products added successfully.", null));
	}
	
	@PostMapping("/remove-products")
	public ResponseEntity<?> removeProduct(@RequestBody List<Long> productIds,Authentication authentication){
		shopServ.removeProduct(productIds,authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true,"Products removed successfully",null));
	}
	
	@GetMapping("/get-shop-product")
	public ResponseEntity<?> getShopProducts(
			@RequestParam(value="productName",required=false) String productName,
			@RequestParam(value="categoryId",required=false)Integer categoryId,
			@RequestParam(value="brandId",required=false) Integer brandId,
			@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="size",defaultValue="20") int size,
			Authentication authentication
			){
			
		String username = authentication.getName();
		Page<ProductProjection> products = shopServ.getShopProducts(productName,categoryId,brandId,page,size,username);
		
		return ResponseEntity.ok(new ApiResponse<>(true,"Shop products fetched successfully.",products));
	}

	@GetMapping("/get-products")
	public ResponseEntity<?> getProducts(
			@RequestParam(value="productName",required=false) String productName,
			@RequestParam(value="categoryId",required=false)Integer categoryId,
			@RequestParam(value="brandId",required=false) Integer brandId,
			@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="size",defaultValue="20") int size
	){
		
		Page<ProductProjection> products = shopServ.getProducts(productName,categoryId,brandId,page,size); 
		
		return ResponseEntity.ok(new ApiResponse<>(true,"Products fetched successfully",products));
	}
	
	@GetMapping("/nearest-shop")
	public ResponseEntity<?> findNearestShopWithProduct(
			@RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude,
			@RequestParam("productId") Long productId
			){
		
		
		List<ShopProjection> shops = shopServ.findNearestShop(latitude,longitude,productId);
		
		return ResponseEntity.ok(new ApiResponse<>(true,"Nearest Shop's find successfully.",shops));
	}
	
}
