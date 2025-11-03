package com.online_e_learning.virtualPathshala.requestDto;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;

public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private Role role;
    private Status status = Status.INACTIVE;

    // Constructors
    public UserRequestDto() {}

    public UserRequestDto(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}