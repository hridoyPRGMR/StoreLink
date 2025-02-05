package com.storelink.controllers.web;


import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.storelink.dto.CategoryDto;
import com.storelink.model.Category;
import com.storelink.services.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
@RequestMapping("/cms/category")
public class CategoryController extends BaseController {
    
    private final CategoryService categoryServ;

    public CategoryController(CategoryService categoryServ){
        this.categoryServ = categoryServ;
    }

    
    @GetMapping("/create")
    public String createCategoryPage(Model model){

        CategoryDto category = new CategoryDto();
        model.addAttribute("category", category);

        return getPageContent(model, "category/create");
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDto req,
        BindingResult res,Model model,RedirectAttributes redirect){

        if(res.hasErrors()){
            return getPageContent(model, "category/create");
        }

        try{
            categoryServ.saveCategory(req);
            redirect.addFlashAttribute("successMessage","Category created successfully.");
        }
        catch(DataIntegrityViolationException e){
            redirect.addFlashAttribute("error","Failed to create category.");
            log.error("Failed to create category. Error: {}",e.getMessage(),e);
        }

        return "redirect:/cms/category/create";
    }

    @GetMapping("/categories")
    public String getCategories(Model model){

        List<Category> categories = categoryServ.getAllCategory();
        model.addAttribute("categories", categories);

        return getPageContent(model, "category/category-list");
    }

    @GetMapping("/update/{id}")
    public String updateCategoryPage(@PathVariable Integer id,Model model){

        Category category = categoryServ.findById(id);
        model.addAttribute("id", id);
        model.addAttribute("category", category);

        return getPageContent(model, "category/update");
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Integer id,
                                 @Valid @ModelAttribute("category") CategoryDto req,
                                 BindingResult res,
                                 Model model,
                                 RedirectAttributes redirect) {
        if (res.hasErrors()) {
            return getPageContent(model, "category/update");
        }
    
        try {
            categoryServ.updateCategory(req, id);
            redirect.addFlashAttribute("successMessage", "Category updated successfully");
        } 
        catch (EntityNotFoundException e) {
            model.addAttribute("error", "Category not found");
            return getPageContent(model, "category/update");
        } 
        catch (Exception e) {
            model.addAttribute("error", "Failed to update category");
            return getPageContent(model, "category/update");
        }
    
        return "redirect:/cms/category/categories";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id,RedirectAttributes redirect){

        try{
            categoryServ.deleteById(id);
            redirect.addFlashAttribute("successMessage","Category deleted successfully.");
        }
        catch(EntityNotFoundException e){
            redirect.addFlashAttribute("error","Category not found!!");
        }
        catch(Exception e){
            redirect.addFlashAttribute("error","Failed to delete category");
        }

        return "redirect:/cms/category/categories";
    }

}
