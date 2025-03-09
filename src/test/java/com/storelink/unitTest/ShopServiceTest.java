package com.storelink.unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storelink.dto.AddressDto;
import com.storelink.dto.ShopDto;
import com.storelink.dto.ShopProductDto;
import com.storelink.model.Address;
import com.storelink.model.Attribute;
import com.storelink.model.Brand;
import com.storelink.model.Category;
import com.storelink.model.Product;
import com.storelink.model.Role;
import com.storelink.model.Shop;
import com.storelink.model.User;
import com.storelink.model.Variation;
import com.storelink.repository.AddressRepository;
import com.storelink.repository.AttributeRepository;
import com.storelink.repository.ShopProductRepository;
import com.storelink.repository.ShopRepository;
import com.storelink.services.ProductService;
import com.storelink.services.ShopService;
import com.storelink.services.UserService;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {
	
	@Mock
	private AddressRepository addressRep;
	
	@Mock
	private ShopRepository shopRep;
	
	@Mock
	private AttributeRepository attributeRep;
	
	@Mock
	private ShopProductRepository shopProdRep;
	
	@Mock
	private UserService userServ;
	
	@Mock 
	private ProductService productServ;
	
	
	@InjectMocks
	private ShopService shopServ;
	
	private ShopDto shopDto;
	private Address mockAddress;
	private Shop mockShop;
	private User mockUser;
	
	private Brand mockBrand;
	private Category mockCategory;
	private Product mockProduct;
	private Variation mockVariation;
	private Attribute mockAttribute;
	
	private ShopProductDto shopProductDto;
	
	private String username = "testUser";
	
	
	@BeforeEach
	void setUp() 
	{
		
		AddressDto addressDto = new AddressDto("street","city","state","country","1980");
		
		shopDto = new ShopDto();
		shopDto.setName("TestShop");
		shopDto.setIntroduction("This shop is for testing.");
		shopDto.setLatitude(24.553607);
		shopDto.setLongitude(90.017551);
		shopDto.setAddress(addressDto);
		
		
		mockAddress = new Address(1L,"street","city","state","country","1980");
		
		mockShop = new Shop();
		mockAddress.setId(1L);
		mockShop.setName("TestShop");
		mockShop.setIntroduction("This shop is for testing.");
		mockShop.setLatitude(24.553607);
		mockShop.setLongitude(90.017551);
		mockShop.setAddress(mockAddress);
		
		mockUser = new User();
		mockUser.setId(1L);
		mockUser.setName("Test User");
		mockUser.setUsername(username);
		mockUser.setEmail("testuser@gmail.com");
		mockUser.setPassword("testuser");
		mockUser.setRoles(Set.of(Role.builder().id(1L).name("ROLE_USER").build()));
		
		
		mockBrand = Brand.builder().id(1).name("TestBrand").description("This Brand is for testing").build();
		
		mockCategory = Category.builder().id(1).name("TestCategory").description("This Category is for testing.").build();
		
		mockVariation = Variation.builder().id(1L).attributeType("Weight").unit("kg").build();
		
		mockAttribute = Attribute.builder().id(1L).sku("test-pr-123").value("100").price(100.00).variation(mockVariation).build();
		
		mockProduct = Product.builder().id(1L).name("TestProduct").brand(mockBrand).category(mockCategory).variation(mockVariation).build();
		
		
		shopProductDto = new ShopProductDto();
        shopProductDto.setAttributeId(1L);
        shopProductDto.setStock(10);
	}
	
	@Test
	void saveShopTest() {

	    when(addressRep.save(any(Address.class))).thenReturn(mockAddress);
	    when(userServ.findByUsername(username)).thenReturn(mockUser);
	    when(shopRep.save(any(Shop.class))).thenAnswer(invocation -> invocation.getArgument(0));


	    Shop savedShop = shopServ.saveShop(shopDto, username);

	    verify(addressRep, times(1)).save(any(Address.class));
	    verify(userServ, times(1)).findByUsername(username);
	    verify(shopRep, times(1)).save(any(Shop.class));


	    assertNotNull(savedShop);
	    assertEquals("TestShop", savedShop.getName());
	    assertEquals(mockAddress, savedShop.getAddress());
	    assertNotNull(savedShop.getUser());
	    assertEquals(mockUser, savedShop.getUser());
	}

	
	@Test
	void addProductsTest() {
		
		mockUser.setShop(mockShop);
		mockVariation.setProduct(mockProduct);
		
		when(userServ.findByUsername(username)).thenReturn(mockUser);
		when(attributeRep.findAllById(anyList())).thenReturn(Collections.singletonList(mockAttribute));
		when(shopProdRep.findByShopId(mockShop.getId())).thenReturn(Collections.emptySet());
		
		shopServ.addProducts(Collections.singletonList(shopProductDto), username);
		
		verify(shopProdRep,times(1)).saveAll(anyList());
	}
}
