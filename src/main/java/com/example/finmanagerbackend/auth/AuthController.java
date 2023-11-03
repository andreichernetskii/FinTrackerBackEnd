package com.example.finmanagerbackend.auth;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.application_user.UserDetailsImpl;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.payloads.request.LoginRequest;
import com.example.finmanagerbackend.payloads.request.SignupRequest;
import com.example.finmanagerbackend.payloads.response.MessageResponse;
import com.example.finmanagerbackend.payloads.response.UserInfoResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.finmanagerbackend.application_user.Role;


@CrossOrigin( origins = "*", maxAge = 3600 )
@RestController
@RequestMapping( "/api/auth" )
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final ApplicationUserRepository applicationUserRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController( AuthenticationManager authenticationManager,
                           ApplicationUserRepository applicationUserRepository,
                           PasswordEncoder encoder,
                           JwtUtils jwtUtils ) {
        this.authenticationManager = authenticationManager;
        this.applicationUserRepository = applicationUserRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping( "/signin" )
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest ) {
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie( userDetails );

        List<String> roles = userDetails.getAuthorities().stream()
                .map( item -> item.getAuthority() )
                .collect( Collectors.toList() );

        return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, jwtCookie.toString() )
                .body( new UserInfoResponse(
                        userDetails.getUsername(),
                        roles
                ) );
    }

    @PostMapping( "/signup" )
    public ResponseEntity<?> registerUser( @RequestBody SignupRequest signUpRequest ) {
        if ( applicationUserRepository.existsByUsername( signUpRequest.getEmail() ) ) {
            return ResponseEntity.badRequest().body( new MessageResponse( "Error: Email is already taken!" ) );
        }

        // Create new user's account
        ApplicationUser user = new ApplicationUser(
                signUpRequest.getEmail(),
                encoder.encode( signUpRequest.getPassword() ) );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if ( strRoles != null && strRoles.contains( "admin" ) ) {
            roles.add(Role.ROLE_ADMIN );
        }

        roles.add( Role.ROLE_USER );

        user.setRoles(roles );
        applicationUserRepository.save(user );

        return ResponseEntity.ok(new MessageResponse( "User registered successfully!" ) );
}

    @PostMapping( "/signout" )
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, cookie.toString() )
                .body( new MessageResponse( "You've been signed out!" ) );
    }
}
