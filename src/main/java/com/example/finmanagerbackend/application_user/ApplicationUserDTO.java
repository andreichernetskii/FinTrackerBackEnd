package com.example.finmanagerbackend.application_user;

// todo: teraz w ogóle nie jest używany - trzeba coś z tym wymyślić
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
