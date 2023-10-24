package com.example.finmanagerbackend.role;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
public class Role implements GrantedAuthority {
    @Id
    private String name;
    @Transient
    @ManyToMany( mappedBy = "roles")
    private Set<ApplicationUser> applicationUsers;


    public Role() {
    }

    public Role( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<ApplicationUser> getUsers() {
        return applicationUsers;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setUsers( Set<ApplicationUser> applicationUsers ) {
        this.applicationUsers = applicationUsers;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
