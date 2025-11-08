package com.online_e_learning.virtualPathshala.requestDto;

public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String mobile;
    private String role;

    // Constructors
    public SignupRequest() {}

    public SignupRequest(String name, String email, String password, String confirmPassword, String mobile, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.mobile = mobile;
        this.role = role;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}