package com.example.finmanagerbackend.application_user;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

// user table in DB
@Entity
//@Table( name = "app_users" )
public class ApplicationUser {
    @Id
    private String email;
    private String password;
    @ElementCollection
    @Enumerated( EnumType.STRING )
    private Set<Role> roles = new HashSet<>();
    private boolean active;

    public ApplicationUser() {

    }

    public ApplicationUser( String email, String password ) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles( Set<Role> roles ) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive( boolean active ) {
        this.active = active;
    }
}
