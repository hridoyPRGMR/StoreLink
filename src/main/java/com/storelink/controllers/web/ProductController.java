package com.storelink.controllers.web;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.storelink.constants.ProductConstants;
import com.storelink.dto.ProductDto;
import com.storelink.model.Product;
import com.storelink.services.BrandService;
import com.storelink.services.CategoryService;
import com.storelink.services.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("path")
    public String getAllProducts(
        @RequestParam("categoryId") Integer categoryId,
        @RequestParam("brandId") Integer brandId,
        @RequestParam("productName") String productName,
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

        return getPageContent(model, "product/product-list");
    }
    
    
}
