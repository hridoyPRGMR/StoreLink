package com.storelink.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storelink.TestData.TestData;
import com.storelink.dto.ShopProductDto;
import com.storelink.model.Address;
import com.storelink.model.Shop;
import com.storelink.model.User;

import jakarta.persistence.EntityManager;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ShopControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EntityManager entityManager;
	
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
	        DockerImageName.parse("postgis/postgis:latest")
	                .asCompatibleSubstituteFor("postgres:latest")
	).withDatabaseName("testdb")
	 .withUsername("testuser")
	 .withPassword("testpass");
	
	
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}
		
	
	@BeforeEach
	void setUp() {
		
		User testUser = TestData.createUser();
		
		
	}
	
	@Test
	@WithMockUser(username="testuser")
	void testAddProducts() throws Exception
	{	
		List<ShopProductDto> products = Arrays.asList(
			new ShopProductDto(1,10),
			new ShopProductDto(2,20)
		);
		
		String productJson = objectMapper.writeValueAsString(products);
		
		mockMvc.perform(post("/api/shop/add-products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("#.message").value("Products added successfully."));
				
	}
	
}
