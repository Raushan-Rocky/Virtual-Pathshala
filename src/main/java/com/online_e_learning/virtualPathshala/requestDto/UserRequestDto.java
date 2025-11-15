package com.online_e_learning.virtualPathshala.requestDto;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequestDto {
    private String name;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Only for input, not output
    private String password;

    private Role role;
    private Status status = Status.INACTIVE;
    private String mobile;

    // Constructors
    public UserRequestDto() {}

    public UserRequestDto(String name, String email, String password, Role role, String mobile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.mobile = mobile;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @JsonIgnore // Never return password in JSON response
    public String getPassword() { return password; }

    @JsonProperty("password") // Only accept password in JSON input
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}