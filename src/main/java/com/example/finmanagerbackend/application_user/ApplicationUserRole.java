package com.example.finmanagerbackend.application_user;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.finmanagerbackend.application_user.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    APP_USER( Sets.newHashSet() ),
    APP_ADMIN( Sets.newHashSet( USER_READ, USER_WRITE, DATA_READ, DATA_WRITE ) );

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole( Set<ApplicationUserPermission> permissions ) {
        this.permissions = permissions;
    }

    private Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    // todo: ???? jak to działa?
    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> permissions = getPermissions().stream()
                .map( permission -> new SimpleGrantedAuthority( permission.getPermission() ) )
                .collect( Collectors.toSet() );
        permissions.add( new SimpleGrantedAuthority( "ROLE_" + this.name() ) );

        return permissions;
    }
}
