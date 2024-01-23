package com.example.finmanagerbackend.security.application_user;

import com.example.finmanagerbackend.security.application_user.ApplicationUser;
import com.example.finmanagerbackend.security.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.security.application_user.Role;
import com.example.finmanagerbackend.security.application_user.UserDetailsImpl;
import com.example.finmanagerbackend.security.jwt.JwtUtils;
import com.example.finmanagerbackend.security.application_user.request.LoginRequest;
import com.example.finmanagerbackend.security.application_user.request.SignupRequest;
import com.example.finmanagerbackend.security.application_user.response.MessageResponse;
import com.example.finmanagerbackend.security.application_user.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling authentication-related logic
 */
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthService( AuthenticationManager authenticationManager, ApplicationUserRepository applicationUserRepository, PasswordEncoder encoder, JwtUtils jwtUtils ) {
        this.authenticationManager = authenticationManager;
        this.applicationUserRepository = applicationUserRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    // Method to get the currently logged-in user
    public ApplicationUser getLoggedUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = ( UserDetails ) principal;

            return applicationUserRepository.findById( userDetails.getUsername() ).orElseThrow();

        } catch ( ClassCastException | NullPointerException e ) {
            throw new AuthenticationException("User not logged") {};
        }
    }

    // Authenticates a user and generates a JWT token.
    public ResponseEntity<?> authenticateUser( LoginRequest loginRequest ) {

        // Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword() ) );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication( authentication );

        // Set user activity status as active in the database
        applicationUserRepository.setUserActivity( loginRequest.getEmail(), true );

        // Retrieve UserDetails from the authenticated principal
        UserDetailsImpl userDetails = ( UserDetailsImpl ) authentication.getPrincipal();

        // Generate a JWT token and create a response cookie
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie( userDetails );

        // Extract user roles from UserDetails
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map( item -> item.getAuthority() )
                .collect( Collectors.toList() );

        // Build and return the response containing JWT token and user information
        return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, jwtCookie.toString() )
                .body( new UserInfoResponse(
                        userDetails.getUsername(),
                        roles
                ) );
    }

    // Registers a new user.
    public ResponseEntity<?> registerUser( SignupRequest signUpRequest ) {
        if ( applicationUserRepository.existsByUsername( signUpRequest.getEmail() ) ) {
            return ResponseEntity.badRequest().body( new MessageResponse( "Error: Email is already taken!" ) );
        }

        // Create new user's account
        ApplicationUser user = new ApplicationUser(
                signUpRequest.getEmail(),
                encoder.encode( signUpRequest.getPassword() ) );

        // Set user roles based on the requested roles
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if ( strRoles != null && strRoles.contains( "admin" ) ) {
            roles.add( Role.ROLE_ADMIN );
        }

        roles.add( Role.ROLE_USER );

        user.setRoles( roles );
        applicationUserRepository.save( user );

        return ResponseEntity.ok( new MessageResponse( "User registered successfully!" ) );
    }

    // Logs out a user and invalidates the JWT token.
    public ResponseEntity<?> logoutUser( HttpServletRequest request ) {

        // Parse the JWT token from the request
        String jwt = jwtUtils.parseJwt( request );

        if ( jwt != null ) {
            // Extract the username from the JWT token
            String username = jwtUtils.getUserNameFromJwtToken( jwt );

            // Set user activity status as inactive in the database
            applicationUserRepository.setUserActivity( username, false );
        }

        // Generate a clean JWT cookie
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        // Return a success response with the clean JWT cookie
        return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, cookie.toString() )
                .body( new MessageResponse( "You've been signed out!" ) );
    }
}
