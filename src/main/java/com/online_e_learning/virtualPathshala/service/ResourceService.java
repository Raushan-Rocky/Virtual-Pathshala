package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.ResourceConverter;
import com.online_e_learning.virtualPathshala.exception.ResourceNotFoundException;
import com.online_e_learning.virtualPathshala.exception.LessonNotFoundException;
import com.online_e_learning.virtualPathshala.model.Resource;
import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.repository.ResourceRepository;
import com.online_e_learning.virtualPathshala.repository.LessonRepository;
import com.online_e_learning.virtualPathshala.requestDto.ResourceRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ResourceConverter resourceConverter;

    // CREATE
    public Resource createResource(ResourceRequestDto requestDto) {
        Lesson lesson = lessonRepository.findById(requestDto.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + requestDto.getLessonId()));

        Resource resource = resourceConverter.convertToEntity(requestDto, lesson);
        return resourceRepository.save(resource);
    }

    // READ ALL
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    // READ BY ID
    public Resource getResourceById(int id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    // READ BY LESSON ID
    public List<Resource> getResourcesByLessonId(int lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new LessonNotFoundException("Lesson not found with id: " + lessonId);
        }
        return resourceRepository.findByLessonId(lessonId);
    }

    // READ BY FILE TYPE
    public List<Resource> getResourcesByFileType(String fileType) {
        return resourceRepository.findByFileType(fileType);
    }

    // SEARCH RESOURCES
    public List<Resource> searchResources(String query) {
        return resourceRepository.searchResources(query);
    }

    // ✅ FIXED: GET LATEST RESOURCES by ID (since createdAt field doesn't exist)
    public List<Resource> getLatestResources() {
        return resourceRepository.findTop10ByOrderByIdDesc();
    }

    // SEARCH BY FILE NAME
    public List<Resource> searchByFileName(String fileName) {
        return resourceRepository.findByFileNameContainingIgnoreCase(fileName);
    }

    // SEARCH BY FILE TYPE
    public List<Resource> searchByFileType(String fileType) {
        return resourceRepository.findByFileTypeContainingIgnoreCase(fileType);
    }

    // UPDATE
    public Resource updateResource(int id, ResourceRequestDto requestDto) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        // If lesson is being updated, validate new lesson exists
        if (requestDto.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(requestDto.getLessonId())
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + requestDto.getLessonId()));
            resource.setLesson(lesson);
        }

        resourceConverter.updateEntityFromDto(requestDto, resource);
        return resourceRepository.save(resource);
    }

    // DELETE
    public void deleteResource(int id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
}