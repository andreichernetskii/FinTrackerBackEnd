package com.example.finmanagerbackend.auth;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.application_user.UserDetailsImpl;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.payloads.request.LoginRequest;
import com.example.finmanagerbackend.payloads.request.SignupRequest;
import com.example.finmanagerbackend.payloads.response.MessageResponse;
import com.example.finmanagerbackend.payloads.response.UserInfoResponse;
import com.example.finmanagerbackend.role.Role;
import com.example.finmanagerbackend.role.RoleRepository;
import com.example.finmanagerbackend.role.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
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


@CrossOrigin( origins = "*", maxAge = 3600 )
@RestController
@RequestMapping( "/api/auth" )
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

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

        if ( strRoles == null ) {
            Role userRole = roleRepository.findByName( UserRoles.ROLE_USER )
                    .orElseThrow( () -> new RuntimeException( "Error: Role is not found." ) );
            roles.add( userRole );
        } else {
            strRoles.forEach( role -> {
                switch ( role ) {
                    case "admin":
                        Role adminRole = roleRepository.findByName( UserRoles.ROLE_ADMIN )
                                .orElseThrow( () -> new RuntimeException( "Error: Role is not found." ) );
                        roles.add( adminRole );

                        break;
                    default:
                        Role userRole = roleRepository.findByName( UserRoles.ROLE_USER )
                                .orElseThrow( () -> new RuntimeException( "Error: Role is not found." ) );
                        roles.add( userRole );
                }
            } );
        }

        user.setRoles( roles );
        applicationUserRepository.save( user );

        return ResponseEntity.ok( new MessageResponse( "User registered successfully!" ) );
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
