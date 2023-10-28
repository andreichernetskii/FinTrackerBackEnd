package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.application_user.ApplicationUserService;
import com.example.finmanagerbackend.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

// here we can customize spring security login, logout and other security configurations
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    public SecurityConfig( PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService ) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    @Bean
    public SecurityFilterChain configureChain( HttpSecurity httpSecurity ) throws Exception {
        return httpSecurity
                .csrf( customizer -> customizer.disable() )
                .sessionManagement( customizer -> customizer.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .addFilter(
                        new JwtUsernameAndPasswordAuthenticationFilter(
                                authenticationManager(
                                        httpSecurity.getSharedObject( AuthenticationConfiguration.class ) ) ) )
                .authorizeHttpRequests( customizer ->
                        customizer.anyRequest().authenticated() )
//                .httpBasic( Customizer.withDefaults() )
//                .userDetailsService( applicationUserService )
                .authenticationProvider( authenticationProvider() )
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
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
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
