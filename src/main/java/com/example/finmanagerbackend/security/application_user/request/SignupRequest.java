package com.example.finmanagerbackend.security.application_user.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private Set<String> role;
    private String password;
    private boolean demo;
}
