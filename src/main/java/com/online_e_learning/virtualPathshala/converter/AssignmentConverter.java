package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.requestDto.AssignmentRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AssignmentConverter {

    public Assignment convertToEntity(AssignmentRequestDto requestDto, User user, Course course) {
        Assignment assignment = new Assignment();
        assignment.setTitle(requestDto.getTitle());
        assignment.setDescription(requestDto.getDescription());
        assignment.setDueDate(requestDto.getDueDate());
        assignment.setType(requestDto.getType() != null ? requestDto.getType() : "HOMEWORK"); // ✅ SET TYPE
        assignment.setUser(user);
        assignment.setCourse(course);
        return assignment;
    }

    public void updateEntityFromDto(AssignmentRequestDto requestDto, Assignment assignment) {
        if (requestDto.getTitle() != null) {
            assignment.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            assignment.setDescription(requestDto.getDescription());
        }
        if (requestDto.getDueDate() != null) {
            assignment.setDueDate(requestDto.getDueDate());
        }
        if (requestDto.getType() != null) { // ✅ UPDATE TYPE
            assignment.setType(requestDto.getType());
        }
    }

    public AssignmentRequestDto convertToResponseDto(Assignment assignment) {
        AssignmentRequestDto responseDto = new AssignmentRequestDto();
        responseDto.setTitle(assignment.getTitle());
        responseDto.setDescription(assignment.getDescription());
        responseDto.setDueDate(assignment.getDueDate());
        responseDto.setType(assignment.getType()); // ✅ ADD TYPE

        if (assignment.getUser() != null) {
            responseDto.setUserId(assignment.getUser().getId());
        }
        if (assignment.getCourse() != null) {
            responseDto.setCourseId(assignment.getCourse().getId());
        }

        return responseDto;
    }
}