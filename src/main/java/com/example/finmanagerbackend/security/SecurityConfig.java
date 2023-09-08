package com.example.finmanagerbackend.security;

import com.example.finmanagerbackend.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    // todo: przeanalizować
    @Bean
    public SecurityFilterChain configureChain( HttpSecurity httpSecurity, UserService userService ) throws Exception {
        return httpSecurity.csrf( customizer -> customizer.disable() )
                .headers( customizer -> customizer.disable() )
                .authorizeHttpRequests( customizer -> customizer.anyRequest().authenticated() )
                .httpBasic( Customizer.withDefaults() )
                .userDetailsService( userService )
                .build();
    }

//    @Bean
//    public InMemoryUserDetailsManager configureUserManager(PasswordEncoder passEncoder) {
//        UserDetails user1 = new User( "user1", passEncoder.encode( "123" ), new ArrayList<>() );
//        UserDetails user2 = new User( "user2", passEncoder.encode( "123" ), new ArrayList<>() );
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager( user1, user2 );
//        return manager;
//    }

    @Bean
    public PasswordEncoder configurePasswordEncoder() {
        PasswordEncoder passEncoder = new BCryptPasswordEncoder();
        return passEncoder;
    }
}
