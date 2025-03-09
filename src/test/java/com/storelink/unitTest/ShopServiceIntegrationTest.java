package com.storelink.unitTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.storelink.services.UserService;


@SpringBootTest
@Testcontainers
public class ShopServiceIntegrationTest {
	
	@Autowired
	private UserService userServ;
	
	
	
}
