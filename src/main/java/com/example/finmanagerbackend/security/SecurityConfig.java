package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.security.application_user.UserDetailsServiceImpl;
import com.example.finmanagerbackend.security.jwt.AuthEntryPointJwt;
import com.example.finmanagerbackend.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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
    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${app.cors.allowed.origin}")
    private String allowedOrigin;

    @Value("${app.cors.allowed.methods}")
    private String allowedMethods;

    @Value("${app.cors.allowed.headers}")
    private String allowedHeaders;



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
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {

        RequestMatcher h2ConsoleMatcher = new AntPathRequestMatcher("/console/**");
        RequestMatcher authApiMatcher = new AntPathRequestMatcher("/api/auth/**");

        // Swagger matchers
        RequestMatcher swaggerUiMatcher = new AntPathRequestMatcher("/swagger-ui/**");
        RequestMatcher swaggerHtmlMatcher = new AntPathRequestMatcher("/swagger-ui.html");
        RequestMatcher apiDocsMatcher = new AntPathRequestMatcher("/v3/api-docs/**");

        http
                .cors(cors -> cors.configurationSource( request -> {

                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(Collections.singletonList(allowedOrigin));
                    config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
                    config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
                    config.setAllowCredentials(true);

                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .exceptionHandling( exception -> exception.authenticationEntryPoint( unauthorizedHandle ) )
                .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .addFilterBefore( authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class )
                .authorizeHttpRequests( auth ->
                        auth
                                .requestMatchers(authApiMatcher).permitAll()
                                .requestMatchers(h2ConsoleMatcher).permitAll()
                                .requestMatchers(swaggerUiMatcher).permitAll()
                                .requestMatchers(swaggerHtmlMatcher).permitAll()
                                .requestMatchers(apiDocsMatcher).permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider( authenticationProvider() );

        return http.build();
    }
}
