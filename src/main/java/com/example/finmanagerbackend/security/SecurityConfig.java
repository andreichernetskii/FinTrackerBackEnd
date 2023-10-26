package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.application_user.ApplicationUserRole;
import com.example.finmanagerbackend.application_user.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.finmanagerbackend.application_user.ApplicationUserRole.APP_ADMIN;

// here we can customize spring security login, logout and other security configurations
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig( PasswordEncoder passwordEncoder ) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain configureChain( HttpSecurity httpSecurity, ApplicationUserService applicationUserService ) throws Exception {
        return httpSecurity
                .csrf( customizer -> customizer.disable() )
//                .sessionManagement( customizer ->
//                        customizer.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
//                .addFilter( new JwtUsernameAndPasswordAuthenticationFilter(  ) )
                .headers( customizer -> customizer.disable() )
                .httpBasic( Customizer.withDefaults() )
                .authorizeHttpRequests( customizer ->
                        customizer.anyRequest().permitAll() )
                .userDetailsService( applicationUserService )
                .build();
    }

    // admin user
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username( "admin" )
                .password( passwordEncoder.encode( "123" ) )
//                .roles( APP_ADMIN.name() )
                .authorities( APP_ADMIN.getGrantedAuthorities() )
                .build();

        return new InMemoryUserDetailsManager( admin );
    }
}
