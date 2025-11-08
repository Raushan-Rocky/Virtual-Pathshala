package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.UserConverter;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.exception.EmailAlreadyExistsException;
import com.online_e_learning.virtualPathshala.exception.UserNotFoundException;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    public User createUser(UserRequestDto requestDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + requestDto.getEmail());
        }

        User user = userConverter.convertToEntity(requestDto);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public void updateUserRole(int userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setRole(newRole);
        userRepository.save(user);
    }

    public List<User> getUsersByRole(String role) {
        Role userRole = Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(userRole);
    }

    public List<User> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }

    public User updateUser(int id, UserRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists: " + requestDto.getEmail());
            }
        }

        userConverter.updateEntityFromDto(requestDto, user);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}