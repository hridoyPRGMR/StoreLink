package com.storelink.controllers.web;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storelink.services.ImageService;

@Controller
@RequestMapping("/cms/images")
public class ImageController {

    private final ImageService imageServ;

    public ImageController(ImageService imageServ) {
        this.imageServ = imageServ;
    }

    @GetMapping("/{folder}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String folder, @PathVariable String imageName) {
        try {
           
            Resource resource = imageServ.getImage(folder, imageName);
            String contentType = imageServ.getContentType(folder, imageName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } 
        catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // @PostMapping("/deletes/{folder}")
    // public String deleteImages(
    //         @PathVariable String folder,
    //         @RequestParam(value = "imagesToDelete", required = false) List<String> imagesToDelete,
    //         RedirectAttributes redirect) {
    
    //     if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
    //         try {
    //             imageServ.deleteImages(imagesToDelete, folder);
    //             redirect.addFlashAttribute("successMessage", "Selected images deleted successfully!");
    //         } catch (IOException e) {
    //             redirect.addFlashAttribute("errorMessage", "Error deleting images: " + e.getMessage());
    //         }
    //     } else {
    //         redirect.addFlashAttribute("errorMessage", "No images were selected.");
    //     }
    
    //     return "redirect:/cms/product/edit"; 
    // }
    
}
