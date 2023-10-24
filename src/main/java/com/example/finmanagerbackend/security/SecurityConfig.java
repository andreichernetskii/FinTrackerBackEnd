package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.application_user.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// here we can customize spring security login, logout and other security configurations
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private String[] ENDPOINT_WHITELIST = {

    };
    // todo: przeanalizować

    // configureChain - łańcuch filtrów do obrabiania zapytań bezpieczeństwa
    // HttpSecurity - pozwala nastawić parametry bezpeczeństwa zapytań HTTP
    // UserService - udostępnia informację o użytkownikach, będzie używany do autorizacji
    @Bean
    public PasswordEncoder configurePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configureChain( HttpSecurity httpSecurity, ApplicationUserService applicationUserService ) throws Exception {
        return httpSecurity
                .csrf( customizer -> customizer.disable() )
                .headers( customizer -> customizer.disable() )
                .authorizeHttpRequests( customizer ->
                        customizer.anyRequest().authenticated() )
                .httpBasic( Customizer.withDefaults() )
                .userDetailsService( applicationUserService )
                .build();
    }

// service of keeping user data in memory
//    @Bean
//    public InMemoryUserDetailsManager configureUserManager(PasswordEncoder passEncoder) {
//        UserDetails user1 = new User( "user1", passEncoder.encode( "123" ), new ArrayList<>() );
//        UserDetails user2 = new User( "user2", passEncoder.encode( "123" ), new ArrayList<>() );
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager( user1, user2 );
//        return manager;

//    }
    // password encryption type
}
