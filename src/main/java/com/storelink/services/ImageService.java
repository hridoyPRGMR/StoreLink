package com.storelink.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class ImageService {
    
    @Value("${image.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IllegalStateException("uploadDir is not configured properly.");
        }

        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }

    public String saveImage(MultipartFile file, String folderName) throws IOException {

        File folder = new File(uploadDir, folderName);
        if (!folder.exists()) {
            folder.mkdirs();  // Create folder if it doesn't exist
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IOException("Invalid file name: null");
        }

        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-_]", "_");
        String fileName = UUID.randomUUID() + "_" + sanitizedFileName;

        Path filePath = Paths.get(uploadDir, folderName, fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }


    public Resource getImage(String folder, String imageName) throws IOException {
        // Construct the file path using the base directory, folder, and image name
        Path filePath = Paths.get(uploadDir, folder, imageName);
        
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new IOException("Image not found or unreadable: " + filePath);
        }
        
        return new UrlResource(filePath.toUri());
    }
    
    /**
     * Optionally, you can also move content-type detection here if you prefer.
     */
    public String getContentType(String folder, String imageName) throws IOException {
        Path filePath = Paths.get(uploadDir, folder, imageName);
        String contentType = Files.probeContentType(filePath);
        return (contentType != null) ? contentType : "application/octet-stream";
    }

    public void deleteImages(List<String> imagesToDelete, String folder) throws IOException {
        for (String image : imagesToDelete) {
            deleteImage(image, folder);
        }
    }
    
    public void deleteImage(String imageName, String folder) throws IOException {
        Path filePath = Paths.get(uploadDir, folder, imageName);
    
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("Image not found: " + imageName);
        }
    }
    

}
