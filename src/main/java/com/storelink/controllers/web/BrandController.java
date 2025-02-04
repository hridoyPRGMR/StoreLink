package com.storelink.controllers.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.storelink.dto.BrandDto;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Brand;
import com.storelink.services.BrandService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/cms/brand")
public class BrandController extends BaseController{
    
    private final BrandService brandServ;

    public BrandController(BrandService brandServ){
        this.brandServ = brandServ;
    }

    @GetMapping("/create")
    public String createBrandPage(Model model){

        Brand brand = new Brand();
        model.addAttribute("brand", brand);

        return getPageContent(model, "brand/create");
    }

    @PostMapping("/create")
    public String createBrand(@Valid @ModelAttribute("brand") BrandDto req,
            BindingResult res,
            Model model,
            RedirectAttributes redirect
        ){

            if(res.hasErrors()){
                return getPageContent(model, "brand/create");
            }

            try{
                brandServ.saveBrand(req);
                redirect.addFlashAttribute("successMessage", "Brand created successfully.");
            }
            catch(DataIntegrityViolationException e){
                model.addAttribute("error", "Brand name already exist");
            }
            catch(Exception e){
                model.addAttribute("error", "Unexpected error occured.");
            }

        return "redirect:/cms/brand/create";
    }

    @GetMapping("/brands")
    public String getBrands(Model model,
        @RequestParam(value = "search",required = false,defaultValue = "") String searchTerm,
		@RequestParam(value = "page",defaultValue = "0") int page,
		@RequestParam(value = "size",defaultValue = "10") int size
    ){
        Page<Brand> brands = brandServ.getAllPaginatedBrands(page,size,searchTerm);
        model.addAttribute("brands",brands.getContent());

        model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("search", searchTerm);

        return getPageContent(model, "brand/brand-list");
    }

    @GetMapping("/update/{id}")
    public String getMethodName(@PathVariable("id") int id,Model model) {
        
        try{
            model.addAttribute("brand", brandServ.findByBrandId(id));
            model.addAttribute("id", id);
        }
        catch(ResourceNotFoundException e){
            model.addAttribute("error", "Brand not found.");
        }
        catch(Exception e){
            model.addAttribute("error", "Unexpected error occured!!");
        }
        
        return getPageContent(model, "brand/update");
    }

    @PostMapping("/update/{id}")
    public String updateBrand(
            @Valid @ModelAttribute("brand") BrandDto req,
            @PathVariable("id") int id,
            BindingResult res,
            Model model,
            RedirectAttributes redirect
    ) {
        if (res.hasErrors()) {
            return getPageContent(model, "brand/update");
        }
    
        try {
            brandServ.updateBrand(req, id);
            redirect.addFlashAttribute("successMessage", "Brand updated successfully.");
            return "redirect:/cms/brand/brands";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Brand not found.");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Brand name must be unique.");
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred.");
        }
    
        return getPageContent(model, "brand/update");
    }

    
    
    
    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable("id") int id,RedirectAttributes redirect){

        try{
            brandServ.deleteBrandById(id);
        }
        catch(ResourceNotFoundException e){
            redirect.addFlashAttribute("error", "Brand not found.");
        }
        catch(Exception e){
            redirect.addFlashAttribute("error", "Unexpected error occured,Faild to delete brand with id: "+id);
        }

        return "redirect:/cms/brand/brands";
    }

}
