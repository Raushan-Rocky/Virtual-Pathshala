package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.requestDto.SubmissionRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SubmissionConverter {

    public Submission convertToEntity(SubmissionRequestDto requestDto, User user, Assignment assignment) {
        Submission submission = new Submission();
        submission.setFilePath(requestDto.getFilePath());
        submission.setGrade(requestDto.getGrade());
        submission.setFeedback(requestDto.getFeedback());
        submission.setUser(user);
        submission.setAssignment(assignment);
        return submission;
    }

    public void updateEntityFromDto(SubmissionRequestDto requestDto, Submission submission) {
        if (requestDto.getFilePath() != null) {
            submission.setFilePath(requestDto.getFilePath());
        }
        if (requestDto.getGrade() != null) {
            submission.setGrade(requestDto.getGrade());
        }
        if (requestDto.getFeedback() != null) {
            submission.setFeedback(requestDto.getFeedback());
        }
    }

    public SubmissionRequestDto convertToResponseDto(Submission submission) {
        SubmissionRequestDto responseDto = new SubmissionRequestDto();
        responseDto.setFilePath(submission.getFilePath());
        responseDto.setGrade(submission.getGrade());
        responseDto.setFeedback(submission.getFeedback());

        if (submission.getUser() != null) {
            responseDto.setUserId(submission.getUser().getId());
        }
        if (submission.getAssignment() != null) {
            responseDto.setAssignmentId(submission.getAssignment().getId());
        }

        return responseDto;
    }
}