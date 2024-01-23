package com.example.finmanagerbackend.security.jwt;

import com.example.finmanagerbackend.security.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.security.application_user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.slf4j.Logger;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private static final Logger logger = LoggerFactory.getLogger( AuthTokenFilter.class );

//    What we do inside doFilterInternal():
//              – get JWT from the HTTP Cookies
//              – if the request has JWT, validate it, parse username from it
//              – from username, get UserDetails to create an Authentication object
//              – set the current UserDetails in SecurityContext using setAuthentication(authentication) method.

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain ) throws ServletException, IOException {

        try {
            String jwt = jwtUtils.parseJwt( request );

            if ( jwt != null && jwtUtils.validateJwtToken( jwt ) ) {
                String username = jwtUtils.getUserNameFromJwtToken( jwt );

                if ( !applicationUserRepository.isUserActive( username ) ) {
                    throw new Exception( "Cannot set user authentication: {}" );
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername( username );

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                SecurityContextHolder.getContext().setAuthentication( authentication );
            }
        } catch ( Exception e ) {
            logger.error( "Cannot set user authentication: {}", e );
        }

        filterChain.doFilter( request, response );
    }
}
