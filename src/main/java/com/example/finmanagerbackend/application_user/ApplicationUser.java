package com.example.finmanagerbackend.application_user;

import jakarta.persistence.*;

@Entity
@Table( name = "users" )
public class ApplicationUser {
    @Id
    private String email;
    @Column( nullable = false )
    private String password;

    public ApplicationUser() {
    }

    public ApplicationUser( String email, String password ) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEMail( String email ) {
        this.email = email;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
}
