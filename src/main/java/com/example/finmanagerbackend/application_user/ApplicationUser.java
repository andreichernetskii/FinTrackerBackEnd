package com.example.finmanagerbackend.application_user;

import com.example.finmanagerbackend.account.Account;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    private boolean active;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private Account account;

    public ApplicationUser() {

    }

    public ApplicationUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.account = new Account();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Account getAccount() {
        return this.account;
    }
}
