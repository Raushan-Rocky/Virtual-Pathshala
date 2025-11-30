package com.online_e_learning.virtualPathshala.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${app.file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    /**
     * Store file under given folder name inside root upload dir.
     * Example:
     *  folder = "course-thumbnails"
     *  -> uploads/course-thumbnails/<generated-name>.png
     *
     * @return relative path like "course-thumbnails/abc123.png"
     */
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Cannot store empty file");
        }

        try {
            // Clean original filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";

            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex != -1) {
                extension = originalFilename.substring(dotIndex);
            }

            // Unique filename: timestamp + random UUID + extension
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String fileName = timestamp + "_" + randomPart + extension;

            // If folder null/blank, use ""
            String safeFolder = (folder == null) ? "" : folder.trim().replace("\\", "/");
            if (safeFolder.startsWith("/")) {
                safeFolder = safeFolder.substring(1);
            }
            if (safeFolder.endsWith("/")) {
                safeFolder = safeFolder.substring(0, safeFolder.length() - 1);
            }

            Path targetDir = (safeFolder.isEmpty())
                    ? this.rootLocation
                    : this.rootLocation.resolve(safeFolder);

            Files.createDirectories(targetDir);

            Path targetFile = targetDir.resolve(fileName);

            // Copy file
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            // Return relative path e.g. "course-thumbnails/20241130120000_xxxxxx.png"
            return safeFolder.isEmpty() ? fileName : safeFolder + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
}
