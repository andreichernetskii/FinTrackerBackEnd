package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.security.application_user.UserDetailsServiceImpl;
import com.example.finmanagerbackend.security.jwt.AuthEntryPointJwt;
import com.example.finmanagerbackend.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * Security configuration class that defines security-related beans and settings.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandle;

    // Creates an instance of the AuthTokenFilter bean.
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // Creates an instance of the DaoAuthenticationProvider bean.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService( userDetailsService );
        authProvider.setPasswordEncoder( passwordEncoder() );

        return authProvider;
    }

    // Creates an instance of the AuthenticationManager bean.
    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration authConfig ) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    // Creates an instance of the BCryptPasswordEncoder bean.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configures the security filter chain.
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http, HandlerMappingIntrospector introspector ) throws Exception {

        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder( introspector );

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("https://finman-project.duckdns.org")); // setting up the frontend
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true); // allow cookies and authorisation through sessions
                    config.setAllowedHeaders(List.of("Content-Type", "Authorization", "Origin", "Accept", "X-Requested-With"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf( csrf -> csrf.disable() )
//                .cors( cors -> cors.disable() )
                .headers( headers -> headers.disable() )
                .exceptionHandling( exception -> exception.authenticationEntryPoint( unauthorizedHandle ) )
                .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider( authenticationProvider() )
                .addFilterBefore( authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class )
                .authorizeHttpRequests( auth ->
                        auth
//                                .requestMatchers( mvcMatcherBuilder.pattern( "/console/**" ) ).permitAll()
                                .requestMatchers( mvcMatcherBuilder.pattern( "/api/auth/**" ) ).permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                );

        return http.build();
    }
}
