package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Resource;
import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.requestDto.ResourceRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ResourceConverter {

    public Resource convertToEntity(ResourceRequestDto requestDto, Lesson lesson) {
        Resource resource = new Resource();
        resource.setFileName(requestDto.getFileName());
        resource.setFileType(requestDto.getFileType());
        resource.setLesson(lesson);
        return resource;
    }

    public void updateEntityFromDto(ResourceRequestDto requestDto, Resource resource) {
        if (requestDto.getFileName() != null) {
            resource.setFileName(requestDto.getFileName());
        }
        if (requestDto.getFileType() != null) {
            resource.setFileType(requestDto.getFileType());
        }
    }

    public ResourceRequestDto convertToResponseDto(Resource resource) {
        ResourceRequestDto responseDto = new ResourceRequestDto();
        responseDto.setFileName(resource.getFileName());
        responseDto.setFileType(resource.getFileType());

        if (resource.getLesson() != null) {
            responseDto.setLessonId(resource.getLesson().getId());
        }

        return responseDto;
    }
}