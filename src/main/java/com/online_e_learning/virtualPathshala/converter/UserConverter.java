package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.requestDto.UserRequestDto;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class UserConverter {

    public User convertToEntity(UserRequestDto requestDto) {
        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());

        // Handle null password
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPasswordHash(hashPassword(requestDto.getPassword()));
        } else {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        user.setRole(requestDto.getRole());
        user.setStatus(requestDto.getStatus());
        return user;
    }

    public void updateEntityFromDto(UserRequestDto requestDto, User user) {
        if (requestDto.getName() != null) {
            user.setName(requestDto.getName());
        }
        if (requestDto.getEmail() != null) {
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getRole() != null) {
            user.setRole(requestDto.getRole());
        }
        if (requestDto.getStatus() != null) {
            user.setStatus(requestDto.getStatus());
        }
        // Only update password if provided
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPasswordHash(hashPassword(requestDto.getPassword()));
        }
    }

    public UserRequestDto convertToResponseDto(User user) {
        UserRequestDto responseDto = new UserRequestDto();
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        responseDto.setStatus(user.getStatus());
        // Password is not returned in response for security
        return responseDto;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}