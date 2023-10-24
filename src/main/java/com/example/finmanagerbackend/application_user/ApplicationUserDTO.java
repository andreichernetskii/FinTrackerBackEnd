package com.example.finmanagerbackend.application_user;

public class ApplicationUserDTO {
    private String email;
    private String password;

    public ApplicationUserDTO() {
    }

    public ApplicationUserDTO( String email, String password ) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
