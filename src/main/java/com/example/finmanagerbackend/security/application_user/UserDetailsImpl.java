package com.example.finmanagerbackend.security.application_user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UserDetails implementation representing a user's details for authentication
 */
public class UserDetailsImpl implements UserDetails {
    private String email;
    @JsonIgnore
    private String password; // Passwords will not be included in the token
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl( String email, String password, Collection<? extends GrantedAuthority> authorities ) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Static method to build a UserDetailsImpl instance from an ApplicationUser
    public static UserDetailsImpl build(ApplicationUser applicationUser) {

        List<GrantedAuthority> authorities = applicationUser.getRoles().stream()
                .map( role -> new SimpleGrantedAuthority( role.name() ) )
                .collect( Collectors.toList());

        return new UserDetailsImpl(
                applicationUser.getEmail(),
                applicationUser.getPassword(),
                authorities
        );
    }

    // todo: i tutaj by w AppUser ta metoda przemapowała role z AppUser
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // for now, it's not useful like field in class
    // it is enough to be like this
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Equals method based on email for comparing UserDetailsImpl instances
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null || getClass() != obj.getClass() ) return false;

        UserDetailsImpl user = ( UserDetailsImpl ) obj;

        return Objects.equals( email, user.email );
    }
}