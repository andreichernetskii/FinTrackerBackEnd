package com.example.finmanagerbackend.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger( AuthEntryPointJwt.class );

    // This method will be triggered anytime unauthenticated User
    // requests a secured HTTP resource and an AuthenticationException is thrown.
    @Override
    public void commence( HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException ) throws IOException, ServletException {

        logger.error( "Unauthorized error: {}", authException.getMessage() );
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
