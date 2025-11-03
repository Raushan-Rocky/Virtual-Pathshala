package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.ResourceConverter;
import com.online_e_learning.virtualPathshala.model.Resource;
import com.online_e_learning.virtualPathshala.requestDto.ResourceRequestDto;
import com.online_e_learning.virtualPathshala.service.FileStorageService;
import com.online_e_learning.virtualPathshala.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceConverter resourceConverter;

    @Autowired
    private FileStorageService fileStorageService;

    // CREATE - POST /api/resources
    @PostMapping
    public ResponseEntity<?> createResource(@RequestBody ResourceRequestDto requestDto) {
        try {
            Resource resource = resourceService.createResource(requestDto);
            ResourceRequestDto response = resourceConverter.convertToResponseDto(resource);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Resource created successfully");
            responseBody.put("data", response);
            responseBody.put("resourceId", resource.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // FILE UPLOAD - POST /api/resources/upload
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lessonId") Integer lessonId) {
        try {
            // Store file and get generated file name
            String fileName = fileStorageService.storeFile(file);

            // Create resource record in database
            ResourceRequestDto requestDto = new ResourceRequestDto();
            requestDto.setFileName(file.getOriginalFilename()); // Original file name
            requestDto.setFileType(getFileType(file.getContentType()));
            requestDto.setLessonId(lessonId);

            Resource resource = resourceService.createResource(requestDto);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "File uploaded successfully",
                    "fileName", fileName,
                    "originalFileName", file.getOriginalFilename(),
                    "resourceId", resource.getId(),
                    "fileType", requestDto.getFileType()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // READ ALL - GET /api/resources
    @GetMapping
    public ResponseEntity<?> getAllResources() {
        try {
            List<Resource> resources = resourceService.getAllResources();
            List<ResourceRequestDto> resourceDtos = resources.stream()
                    .map(resourceConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", resourceDtos);
            response.put("count", resourceDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY ID - GET /api/resources/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getResourceById(@PathVariable int id) {
        try {
            Resource resource = resourceService.getResourceById(id);
            ResourceRequestDto response = resourceConverter.convertToResponseDto(resource);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY LESSON ID - GET /api/resources/lesson/{lessonId}
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getResourcesByLessonId(@PathVariable int lessonId) {
        try {
            List<Resource> resources = resourceService.getResourcesByLessonId(lessonId);
            List<ResourceRequestDto> resourceDtos = resources.stream()
                    .map(resourceConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", resourceDtos);
            response.put("count", resourceDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY FILE TYPE - GET /api/resources/type/{fileType}
    @GetMapping("/type/{fileType}")
    public ResponseEntity<?> getResourcesByFileType(@PathVariable String fileType) {
        try {
            List<Resource> resources = resourceService.getResourcesByFileType(fileType);
            List<ResourceRequestDto> resourceDtos = resources.stream()
                    .map(resourceConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", resourceDtos);
            response.put("count", resourceDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - PUT /api/resources/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResource(@PathVariable int id, @RequestBody ResourceRequestDto requestDto) {
        try {
            Resource resource = resourceService.updateResource(id, requestDto);
            ResourceRequestDto response = resourceConverter.convertToResponseDto(resource);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Resource updated successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE - DELETE /api/resources/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable int id) {
        try {
            resourceService.deleteResource(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Resource deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // FILE DOWNLOAD - GET /api/resources/download/{fileName}
    @GetMapping("/download/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.getFilePath(fileName);
            org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(fileName);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getFileType(String contentType) {
        if (contentType == null) return "UNKNOWN";
        switch (contentType) {
            case "application/pdf": return "PDF";
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return "PPT";
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "DOCX";
            case "image/jpeg":
            case "image/png":
            case "image/gif":
                return "IMAGE";
            case "video/mp4":
            case "video/avi":
            case "video/mkv":
                return "VIDEO";
            default: return "OTHER";
        }
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "pdf": return "application/pdf";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "mp4": return "video/mp4";
            case "avi": return "video/avi";
            default: return "application/octet-stream";
        }
    }
}