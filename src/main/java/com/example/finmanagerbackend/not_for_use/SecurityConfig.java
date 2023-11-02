package com.example.finmanagerbackend.not_for_use;

import com.example.finmanagerbackend.application_user.ApplicationUserService;
import com.example.finmanagerbackend.not_for_use.jwt.JwtConfig;
import com.example.finmanagerbackend.not_for_use.jwt.JwtTokenVerifier;
import com.example.finmanagerbackend.not_for_use.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

// here we can customize spring security login, logout and other security configurations
//@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public SecurityConfig( PasswordEncoder passwordEncoder,
                           ApplicationUserService applicationUserService,
                           SecretKey secretKey,
                           JwtConfig jwtConfig ) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecurityFilterChain configureChain( HttpSecurity httpSecurity ) throws Exception {
        return httpSecurity
                .csrf( customizer -> customizer.disable() )
                .authorizeHttpRequests( customizer -> {
//                    customizer.requestMatchers( antMatcher;
                    customizer.anyRequest().permitAll();
                } )
//                .httpBasic( Customizer.withDefaults() )
//                .userDetailsService( applicationUserService )
//                .authenticationProvider( authenticationProvider() )
                .addFilter( new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationManager(
                                httpSecurity.getSharedObject( AuthenticationConfiguration.class ) ), jwtConfig, secretKey ) )
                .addFilterAfter( new JwtTokenVerifier( secretKey, jwtConfig ), JwtUsernameAndPasswordAuthenticationFilter.class )
                // this disables session creation on Spring Security
                .sessionManagement( customizer -> customizer.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder( passwordEncoder ); // allows to encode passwords
        provider.setUserDetailsService( applicationUserService );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // admin user
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.builder()
//                .username( "admin" )
//                .password( passwordEncoder.encode( "123" ) )
////                .roles( APP_ADMIN.name() )
//                .authorities( APP_ADMIN.getGrantedAuthorities() )
//                .build();
//
//        return new InMemoryUserDetailsManager( admin );
//    }
}
