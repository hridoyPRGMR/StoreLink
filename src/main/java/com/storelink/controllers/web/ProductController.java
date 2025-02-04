package com.storelink.controllers.web;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.storelink.constants.ProductConstants;
import com.storelink.dto.ProductDto;
import com.storelink.exceptions.ResourceNotFoundException;
import com.storelink.model.Product;
import com.storelink.services.BrandService;
import com.storelink.services.CategoryService;
import com.storelink.services.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
@RequestMapping("/cms/product")
public class ProductController extends BaseController {
    
    private final ProductService productServ;
    private final CategoryService categoryServ;
    private final BrandService brandServ;

    public ProductController(ProductService productServ,CategoryService categoryServ,BrandService brandServ){
        this.productServ = productServ;
        this.categoryServ = categoryServ;
        this.brandServ = brandServ;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {

        ProductDto product = new ProductDto();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryServ.getAllCategories());
        model.addAttribute("brands", brandServ.getAllBrands());

        model.addAttribute("attributeTypes", ProductConstants.attributeTypes);
        model.addAttribute("units", ProductConstants.units);

        return getPageContent(model, "product/create");
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") ProductDto req,
        BindingResult res,
        RedirectAttributes redirect,
        Model model
    ){

        if(res.hasErrors()){
            model.addAttribute("categories", categoryServ.getAllCategories());
            model.addAttribute("brands", brandServ.getAllBrands());

            model.addAttribute("attributeTypes", ProductConstants.attributeTypes);
            model.addAttribute("units", ProductConstants.units);
            return getPageContent(model, "product/create");
        }

        try{
            productServ.saveProduct(req);
            redirect.addFlashAttribute("successMessage","Product created successfully");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            redirect.addFlashAttribute("error","Failed to create product.");
        }
        
        return "redirect:/cms/product/create";
    }

    @GetMapping("/products")
    public String getAllProducts(
        @RequestParam(value="categoryId",required=false) Integer categoryId,
        @RequestParam(value="brandId",required = false) Integer brandId,
        @RequestParam(value = "productName",required = false) String productName,
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = "10") int size,
        Model model 
    ) {

        Page<Product> products = productServ.getAllPaginatedProducts(categoryId,brandId,productName,page,size);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("productName", productName);
        model.addAttribute("categories", categoryServ.getAllCategories());
        model.addAttribute("brands", brandServ.getAllBrands());
        
        model.addAttribute("productName", productName);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brandId", brandId);

        return getPageContent(model, "product/product-list");
    }
    
    @GetMapping("/view/{id}")
    public String viewProductPage(@PathVariable int id,Model model,RedirectAttributes redirect,HttpServletRequest req) {
        
        try{
            Product product = productServ.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("images", product.getImages());
            model.addAttribute("path",req.getPathInfo());
            return getPageContent(model, "product/view");
        }
        catch(ResourceNotFoundException e){
            redirect.addFlashAttribute("error","Product not found");
        }
        catch(Exception e){
            redirect.addFlashAttribute("error", "Unexpected error occured");
        }

        return "redirect:/cms/product/products";
    }

    @GetMapping("/update/{id}")
    public String updateProductPage(@PathVariable long id,RedirectAttributes redirect,Model model) {
        
        try{
            Product product  = productServ.findById(id);
            model.addAttribute("productId", id);
            model.addAttribute("product",productServ.toDto(product));
            model.addAttribute("productImages",product.getImages());
            model.addAttribute("categories", categoryServ.getAllCategories());
            model.addAttribute("brands", brandServ.getAllBrands());
            model.addAttribute("attributeTypes", ProductConstants.attributeTypes);
            model.addAttribute("units", ProductConstants.units);
            return getPageContent(model, "product/update");
        }
        catch(ResourceNotFoundException e){
            redirect.addFlashAttribute("error",e.getMessage());
        }
        catch(Exception e){
            redirect.addFlashAttribute("error","Unexpected error occured.");
        }
        
        return "redirect:/cms/product/products";
    }

    @PostMapping("/delete/{id}")
    public String postMethodName(@PathVariable long id,RedirectAttributes redirect) {
        
        try{
            productServ.deleteById(id);
            redirect.addFlashAttribute("successMessage", "Product deleted sucessfully.");
        }
        catch(ResourceNotFoundException e){
            redirect.addFlashAttribute("error", "Product not found.");
        }
        catch(IOException e){
            redirect.addFlashAttribute("error","Failed to delete product images");
        }
        catch(Exception e){
            redirect.addFlashAttribute("error", "Unexpected error occured.");
            System.out.println(e.getMessage());
        }
        
        return "redirect:/cms/product/products";
    }
    
    
    @PostMapping("/update/{productId}")
    public String updateProductHandler(@PathVariable long productId, 
        @Valid @ModelAttribute("product") ProductDto req,
        BindingResult res,
        Model model,
        RedirectAttributes redirect) {
        // Load UI data
        model.addAttribute("categories", categoryServ.getAllCategories());
        model.addAttribute("brands", brandServ.getAllBrands());
        model.addAttribute("attributeTypes", ProductConstants.attributeTypes);
        model.addAttribute("units", ProductConstants.units);

        // Validate form
        if (res.hasErrors()) {
            return getPageContent(model, "product/update");
        }

        try {
            productServ.updateProduct(req, productId);
            redirect.addFlashAttribute("successMessage", "Product updated successfully.");
            return "redirect:/cms/product/products";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update product: " + e.getMessage());
            e.printStackTrace();
        }

        return getPageContent(model, "product/update");
    }

    @PostMapping("/delete/image/{id}/{folder}")
    public String deleteImages(
            @PathVariable long id,
            @PathVariable String folder,
            @RequestParam(value = "imagesToDelete", required = false) List<String> imagesToDelete,
            RedirectAttributes redirect) {
    
        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            try {
                productServ.deleteImages(imagesToDelete, folder);
                redirect.addFlashAttribute("successMessage", "Selected images deleted successfully!");
            } catch (IOException e) {
                redirect.addFlashAttribute("error", "Error deleting images: " + e.getMessage());
            }
            catch(Exception e){
                redirect.addFlashAttribute("error", "Unexpected error occured.");
            }
        } else {
            redirect.addFlashAttribute("errorMessage", "No images were selected.");
        }
    
        return "redirect:/cms/product/update/"+id; 
    }

}
