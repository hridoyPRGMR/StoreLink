package com.storelink.controllers.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storelink.apiresponse.ApiResponse;
import com.storelink.dto.ShopDto;
import com.storelink.dto.ShopProductDto;
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

	// get all products with filter
	@GetMapping("/get-products")
	public ResponseEntity<?> getProducts(@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "brandId", required = false) Integer brandId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {

		Page<ProductProjection> products = shopServ.getProducts(productName, categoryId, brandId, page, size);

		return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
	}

	// create shop
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Shop>> createShop(@Valid @RequestBody ShopDto req,
			Authentication authentication) {

		String username = authentication.getName();
		Shop shop = shopServ.saveShop(req, username);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Shop created successfully", shop));

	}

	// add products to shop
	@PostMapping("/add-products")
	public ResponseEntity<?> addProducts(@RequestBody List<ShopProductDto> req, Authentication authentication) {
		System.out.println(req);
		shopServ.addProducts(req, authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Products added successfully.", null));
	}

	//update shop product
	@PutMapping("/update-product")
	public ResponseEntity<?> updateProduct(@RequestParam(value = "attributeId") long attributeId,
			@RequestParam(value = "stock") int stock, Authentication authentication) {
		shopServ.updateProduct(attributeId, stock, authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Product update successfully", null));
	}

	//remove products from shop
	@PostMapping("/remove-products")
	public ResponseEntity<?> removeProduct(@RequestBody List<Long> productIds, Authentication authentication) {
		shopServ.removeProduct(productIds, authentication.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, "Products removed successfully", null));
	}
	
	//get shop product
	@GetMapping("/get-shop-product")
	public ResponseEntity<?> getShopProducts(@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "brandId", required = false) Integer brandId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Authentication authentication) {

		String username = authentication.getName();
		Page<ProductProjection> products = shopServ.getShopProducts(productName, categoryId, brandId, page, size,
				username);

		return ResponseEntity.ok(new ApiResponse<>(true, "Shop products fetched successfully.", products));
	}
	
	
	// get nearest shop
	@GetMapping("/nearest-shop")
	public ResponseEntity<?> findNearestShopWithProduct(@RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude, @RequestParam("productId") Long productId) {

		List<ShopProjection> shops = shopServ.findNearestShop(latitude, longitude, productId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Nearest Shop's find successfully.", shops));
	}

}
