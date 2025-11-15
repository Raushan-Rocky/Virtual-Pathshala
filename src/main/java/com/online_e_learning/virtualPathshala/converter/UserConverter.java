package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.requestDto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User convertToEntity(UserRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());

        // Encrypt password
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(requestDto.getPassword()));
        }

        user.setRole(requestDto.getRole());
        user.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : Status.INACTIVE);
        user.setMobile(requestDto.getMobile());

        return user;
    }

    public UserRequestDto convertToResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserRequestDto responseDto = new UserRequestDto();
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        responseDto.setStatus(user.getStatus());
        responseDto.setMobile(user.getMobile());
        // Never include password in response DTO

        return responseDto;
    }

    public void updateEntityFromDto(UserRequestDto requestDto, User user) {
        if (requestDto == null || user == null) {
            return;
        }

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
        if (requestDto.getMobile() != null) {
            user.setMobile(requestDto.getMobile());
        }
        // Password is handled separately in service layer
    }
}