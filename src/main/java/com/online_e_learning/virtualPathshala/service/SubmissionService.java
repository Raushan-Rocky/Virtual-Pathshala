package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.SubmissionConverter;
import com.online_e_learning.virtualPathshala.exception.SubmissionNotFoundException;
import com.online_e_learning.virtualPathshala.exception.UserNotFoundException;
import com.online_e_learning.virtualPathshala.exception.AssignmentNotFoundException;
import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.repository.SubmissionRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.repository.AssignmentRepository;
import com.online_e_learning.virtualPathshala.requestDto.SubmissionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionConverter submissionConverter;

    // CREATE
    public Submission createSubmission(SubmissionRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        Assignment assignment = assignmentRepository.findById(requestDto.getAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found with id: " + requestDto.getAssignmentId()));

        submissionRepository.findByUserIdAndAssignmentId(requestDto.getUserId(), requestDto.getAssignmentId())
                .ifPresent(submission -> {
                    throw new IllegalArgumentException("Submission already exists for this user and assignment");
                });

        Submission submission = submissionConverter.convertToEntity(requestDto, user, assignment);
        return submissionRepository.save(submission);
    }

    // READ ALL
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // READ BY ID
    public Submission getSubmissionById(int id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission not found with id: " + id));
    }

    // READ BY USER ID
    public List<Submission> getSubmissionsByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return submissionRepository.findByUserId(userId);
    }

    // READ BY ASSIGNMENT ID
    public List<Submission> getSubmissionsByAssignmentId(int assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new AssignmentNotFoundException("Assignment not found with id: " + assignmentId);
        }
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    // READ BY USER AND ASSIGNMENT
    public Submission getSubmissionByUserAndAssignment(int userId, int assignmentId) {
        return submissionRepository.findByUserIdAndAssignmentId(userId, assignmentId)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission not found for user id: " + userId + " and assignment id: " + assignmentId));
    }

    // READ UNGRADED
    public List<Submission> getUngradedSubmissions() {
        return submissionRepository.findByGradeIsNull();
    }

    // READ GRADED
    public List<Submission> getGradedSubmissions() {
        return submissionRepository.findByGradeIsNotNull();
    }

    // UPDATE
    public Submission updateSubmission(int id, SubmissionRequestDto requestDto) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission not found with id: " + id));

        submissionConverter.updateEntityFromDto(requestDto, submission);
        return submissionRepository.save(submission);
    }

    // GRADE SUBMISSION
    public Submission gradeSubmission(int id, Float grade, String feedback) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission not found with id: " + id));

        submission.setGrade(grade);
        if (feedback != null) {
            submission.setFeedback(feedback);
        }

        return submissionRepository.save(submission);
    }

    // DELETE
    public void deleteSubmission(int id) {
        if (!submissionRepository.existsById(id)) {
            throw new SubmissionNotFoundException("Submission not found with id: " + id);
        }
        submissionRepository.deleteById(id);
    }
}