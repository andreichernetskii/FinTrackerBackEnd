package com.example.finmanagerbackend.security.application_user.response;

import java.util.List;

public class UserInfoResponse {
    private String email;
    private List<String> roles;

    public UserInfoResponse(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
