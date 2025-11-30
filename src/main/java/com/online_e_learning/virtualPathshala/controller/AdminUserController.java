package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // Page
    @GetMapping
    public String usersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values()); // ADMIN, TEACHER, STUDENT
        return "admin/users"; // resources/templates/admin/users.html
    }

    // Update role (AJAX)
    @PostMapping("/role")
    @ResponseBody
    public ResponseEntity<?> updateRole(@RequestParam("userId") int userId,
                                        @RequestParam("role") String roleStr) {
        try {
            Role role = Role.valueOf(roleStr);
            userService.updateUserRole(userId, role);
            return ResponseEntity.ok().body("Role updated");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid role");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body("Update failed: " + ex.getMessage());
        }
    }

    // Hard delete user
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body("Delete failed: " + ex.getMessage());
        }
    }
}

