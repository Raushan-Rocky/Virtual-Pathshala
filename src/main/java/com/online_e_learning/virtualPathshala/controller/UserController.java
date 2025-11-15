package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.config.JwtTokenProvider;
import com.online_e_learning.virtualPathshala.converter.UserConverter;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.UserRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.ApiResponse;
import com.online_e_learning.virtualPathshala.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // ✅ Create User - Only ADMIN can create users
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto requestDto) {
        try {
            System.out.println("👤 Creating new user: " + requestDto.getEmail());

            // Validate request
            if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }

            if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }

            // Only ADMIN can create ADMIN users
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (requestDto.getRole() == Role.ADMIN && !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Only admins can create admin users"));
            }

            User user = userService.createUser(requestDto);
            UserRequestDto response = userConverter.convertToResponseDto(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "User created successfully");
            responseBody.put("data", response);
            responseBody.put("userId", user.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("❌ Error creating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ✅ Get All Users - Only ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserRequestDto> userDtos = users.stream()
                    .map(userConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userDtos);
            response.put("count", userDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error getting all users: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error retrieving users: " + e.getMessage()));
        }
    }

    // ✅ Get User by ID - Users can access their own data, ADMIN/TEACHER can access any
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @userSecurity.isOwnProfile(#userId)")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found with ID: " + userId));
            }

            User user = userOptional.get();

            // Create secure response (exclude sensitive data)
            Map<String, Object> userData = createSecureUserData(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "User data retrieved successfully", userData));

        } catch (Exception e) {
            System.out.println("❌ Error getting user by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving user: " + e.getMessage()));
        }
    }

    // ✅ Get User by Email - Users can access their own data, ADMIN/TEACHER can access any
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @userSecurity.isOwnProfileByEmail(#email)")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            UserRequestDto response = userConverter.convertToResponseDto(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error getting user by email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("User not found: " + e.getMessage()));
        }
    }

    // ✅ Get Users by Role - Only ADMIN
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            // Validate role
            try {
                Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Invalid role: " + role));
            }

            List<User> users = userService.getUsersByRole(role);
            List<UserRequestDto> userDtos = users.stream()
                    .map(userConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userDtos);
            response.put("count", userDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error getting users by role: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ✅ Get Teacher Profile - Teachers can access their own profile, ADMIN can access any
    @GetMapping("/teacher/{id}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') and @userSecurity.isTeacherOrAdmin(#id)")
    public ResponseEntity<?> getTeacherProfile(@PathVariable int id) {
        try {
            User user = userService.getUserById(id);

            // Check if user is actually a teacher
            if (!user.getRole().name().equals("TEACHER")) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("User is not a teacher"));
            }

            UserRequestDto response = userConverter.convertToResponseDto(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error getting teacher profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ✅ Update User - Users can update their own profile, ADMIN can update any
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT') and @userSecurity.isOwnProfile(#id)")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserRequestDto requestDto) {
        try {
            // Get current user for authorization check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            // Only ADMIN can change roles
            if (requestDto.getRole() != null && !currentUser.getRole().equals(Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Only admins can change user roles"));
            }

            // Only ADMIN can change status
            if (requestDto.getStatus() != null && !currentUser.getRole().equals(Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Only admins can change user status"));
            }

            User user = userService.updateUser(id, requestDto);
            UserRequestDto response = userConverter.convertToResponseDto(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "User updated successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error updating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ✅ Delete User - Only ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            // Prevent admin from deleting themselves
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            if (currentUser.getId() == id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Cannot delete your own account"));
            }

            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ✅ Get Current User Profile
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.getUserByEmail(email);
            Map<String, Object> userData = createSecureUserData(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", userData));
        } catch (Exception e) {
            System.out.println("❌ Error getting current user profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving profile: " + e.getMessage()));
        }
    }

    // ✅ Change Password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            String currentPassword = passwordRequest.get("currentPassword");
            String newPassword = passwordRequest.get("newPassword");

            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Current password and new password are required"));
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Current password is incorrect"));
            }

            // Update password
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error changing password: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error changing password: " + e.getMessage()));
        }
    }

    // ✅ Helper method to create secure user data (exclude sensitive information)
    private Map<String, Object> createSecureUserData(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole().toString());
        userData.put("mobile", user.getMobile());
        userData.put("status", user.getStatus().toString());
        userData.put("createdAt", user.getCreatedAt());
        userData.put("updatedAt", user.getUpdatedAt());
        // Exclude password hash and other sensitive data
        return userData;
    }

    // ✅ Helper method to create error response
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}