package com.storelink.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.storelink.dto.BrandDto;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Brand;
import com.storelink.repository.BrandRepository;



@Service
public class BrandService {
    
    private final BrandRepository brandRep;
    private final CurrentUserService userService;

    public BrandService(BrandRepository brandRep,CurrentUserService userService){
        this.brandRep=brandRep;
        this.userService = userService;
    }

    public boolean existByName(String name){
        return brandRep.existsByName(name);
    }

    public boolean existById(int id){
        return brandRep.existsById(id);
    }

    public Brand findByBrandId(int id){
        return brandRep.findById(id).orElseThrow(()->new ResourceNotFoundException("Brand not found with id:"+id));
    }

    public void saveBrand(BrandDto req) {
        Brand brand = toEntity(req);
        brand.setCreatedBy(userService.getCurrentUserId());

        brandRep.save(brand);
    }


    public Brand toEntity(BrandDto dto){

        Brand brand = new Brand();

        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());

        return brand;
    }

    public Page<Brand> getAllPaginatedBrands(int page, int size, String searchTerm) {
        
        PageRequest pageable = PageRequest.of(page,size);
    
        if(searchTerm != null && !searchTerm.trim().isEmpty()){
            return brandRep.findByNameContainingIgnoreCase(searchTerm, pageable);
        }

        return brandRep.findAll(pageable);
    }

    public void deleteBrandById(int id){
        if(!existById(id)){
            throw new ResourceNotFoundException("Brand not found with ID: " + id);
        }
        brandRep.deleteById(id);
    }

    public void updateBrand(BrandDto req,int id) {
       
        Brand brand = findByBrandId(id);
        
        if (brandRep.existsByNameAndIdNot(req.getName(), id)) {
            throw new DataIntegrityViolationException("Brand name already exist.");
        }

        brand.setName(req.getName());
        brand.setDescription(req.getDescription());
        brand.setUpdatedBy(userService.getCurrentUserId());

        brandRep.save(brand);
    }
    
    public List<Brand> getAllBrands(){
        return brandRep.findAll();
    }
}
