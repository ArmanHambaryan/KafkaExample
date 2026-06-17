package com.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path storageDir = Paths.get("images");

    public FileStorageService() {
        try {
            Files.createDirectories(storageDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create images directory", e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + extension;

        try {
            Files.copy(file.getInputStream(), storageDir.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }

        return "/images/" + filename;
    }
}