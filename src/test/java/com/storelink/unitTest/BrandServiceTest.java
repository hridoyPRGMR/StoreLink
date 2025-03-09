package com.storelink.unitTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storelink.dto.BrandDto;
import com.storelink.model.Brand;
import com.storelink.repository.BrandRepository;
import com.storelink.services.BrandService;
import com.storelink.services.CurrentUserService;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {
		
	
	@Mock
	private BrandRepository brandRep;
	
	
	@Mock
	private CurrentUserService userServ;
	
	@InjectMocks
	private BrandService brandServ;
	
	private BrandDto brandDto;
	private Brand mockBrand;
	
	@BeforeEach
	void setUp() {
		brandDto = new BrandDto("TestBrand","This brand is for testing.");
		
		mockBrand = brandServ.toEntity(brandDto);
		mockBrand.setName(brandDto.getName());
		mockBrand.setDescription(brandDto.getDescription());
		mockBrand.setCreatedBy(1L);
	}
	
	@Test
	void saveBrandTest() {
		
		when(userServ.getCurrentUserId()).thenReturn(1L);
		when(brandRep.save(any(Brand.class))).thenReturn(mockBrand);
		
		brandServ.saveBrand(brandDto);
		
		verify(brandRep,times(1)).save(any(Brand.class));
	}
	
//	@Test
	void updateBrandTest() {
		
//		when(brandRep.existsByNameAndIdNot(brandDto.getName(), 0))
		
	}
	
}
