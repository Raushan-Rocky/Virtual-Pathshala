package com.online_e_learning.virtualPathshala.security;

import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UserRepository userRepository;

    public boolean isOwnProfile(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Users can access their own profile
        if (currentUser.getId() == userId) {
            return true;
        }

        // ADMIN and TEACHER can access any profile
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_TEACHER"));
    }

    public boolean isOwnProfileByEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Users can access their own profile
        if (currentUsername.equals(email)) {
            return true;
        }

        // ADMIN and TEACHER can access any profile
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_TEACHER"));
    }

    public boolean isTeacherOrAdmin(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Users can access their own profile
        if (currentUser.getId() == userId) {
            return true;
        }

        // ADMIN can access any teacher profile
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}