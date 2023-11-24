package com.example.finmanagerbackend.auth;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.application_user.UserDetailsImpl;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.payloads.request.LoginRequest;
import com.example.finmanagerbackend.payloads.request.SignupRequest;
import com.example.finmanagerbackend.payloads.response.MessageResponse;
import com.example.finmanagerbackend.payloads.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AuthService authService;

    public AuthController( AuthService authService ) {
        this.authService = authService;
    }

    @PostMapping( "/signin" )
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest ) {
        return authService.authenticateUser( loginRequest );
    }

    @PostMapping( "/signup" )
    public ResponseEntity<?> registerUser( @RequestBody SignupRequest signUpRequest ) {
        return authService.registerUser( signUpRequest );
    }

    @PostMapping( "/signout" )
    public ResponseEntity<?> logoutUser( HttpServletRequest request ) {
        return authService.logoutUser( request );
    }
}
