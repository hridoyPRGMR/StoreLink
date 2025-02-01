package com.storelink.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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
}
