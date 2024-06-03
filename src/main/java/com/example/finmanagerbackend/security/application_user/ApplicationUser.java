package com.example.finmanagerbackend.security.application_user;

import com.example.finmanagerbackend.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing the user table in the database with logins and passwords.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser  {
    @Id
    private String email;
    private String password;
    @ElementCollection
    @Enumerated( EnumType.STRING )
    private Set<Role> roles = new HashSet<>();
    private boolean active;
    @OneToOne
    @Cascade( CascadeType.ALL )
    private Account account;
    private boolean demo;

    public ApplicationUser( String email, String password, boolean isDemo ) {
        this.email = email;
        this.password = password;
        this.demo = isDemo;
        this.account = new Account();
        this.account.setDemo( isDemo );
    }
}
