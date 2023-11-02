package com.example.finmanagerbackend.not_for_use.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

// job -> verify credentials
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    public JwtUsernameAndPasswordAuthenticationFilter( AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey ) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    // grab username and password send from client
    @Override
    public Authentication attemptAuthentication( HttpServletRequest request,
                                                 HttpServletResponse response ) throws AuthenticationException {

        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue( request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            return authenticationManager.authenticate( authentication );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    // will be invoked after successful authentication
    // create a token inside and send it to client
    @Override
    protected void successfulAuthentication( HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain chain,
                                             Authentication authResult ) throws IOException, ServletException {

        String token = Jwts.builder()
                .subject( authResult.getName() )
                .claim( "authorities", authResult.getAuthorities() )
                .issuedAt( new Date() )
                .expiration( java.sql.Date.valueOf( LocalDate.now().plusWeeks( 1 ) ) ) // expired after 1 week
                .signWith( secretKey )
                .compact();

        response.addHeader( jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token );
    }
}
