package com.storelink.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.storelink.dto.CategoryDto;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Category;
import com.storelink.repository.CategoryRepository;

@Service
public class CategoryService {
    
    private final CurrentUserService currentUser;
    private final CategoryRepository categoryRep;

    public CategoryService(CategoryRepository categoryRep,CurrentUserService currentUser){
        this.categoryRep = categoryRep;
        this.currentUser = currentUser;
    }

    public Category saveCategory(CategoryDto req) {

        Category category = convertToEntity(req);
        category.setCreatedBy(currentUser.getCurrentUserId());

        return categoryRep.save(category);
    }

    public List<Category> getAllCategory() {
        return categoryRep.findAll();
    }

    public Category findById(Integer id) {
       return categoryRep.findById(id)
                            .orElseThrow(()->new ResourceNotFoundException("Category not found with ID: " + id));
    }

    public void updateCategory(CategoryDto req, Integer id) {
        
        Category category = findById(id);                             
    
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setUpdatedBy(currentUser.getCurrentUserId());
    
        categoryRep.save(category);
    }
    


    private Category convertToEntity(CategoryDto req){

        Category category= new Category();

        category.setName(req.getName());    
        category.setDescription(req.getDescription());
        return category;
    }

    public void deleteById(Integer id) {
        Category category = findById(id);

        categoryRep.delete(category);
    }

    public List<Category> getAllCategories(){
        return categoryRep.findAll();
    }

}
