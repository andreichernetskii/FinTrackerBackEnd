package com.example.finmanagerbackend.role;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table( name = "user_roles" )
public class Role {
    @Id
    @Enumerated( EnumType.STRING )
    private UserRoles name;

    public Role() {}

    public Role( UserRoles name ) {
        this.name = name;
    }

    public UserRoles getName() {
        return name;
    }

    public void setName( UserRoles name ) {
        this.name = name;
    }
}
