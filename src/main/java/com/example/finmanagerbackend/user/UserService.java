package com.example.finmanagerbackend.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passEncoder;

    public UserService( UserRepository userRepository, PasswordEncoder passEncoder ) {
        this.userRepository = userRepository;
        this.passEncoder = passEncoder;
    }

    public void newUser( UserDTO userDTO ) {
        userRepository.save( new User(
                userDTO.getEmail(),
                passEncoder.encode(userDTO.getPassword()) )
        );
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findById( username )
                .orElseThrow(() -> new UsernameNotFoundException( "User nie istnieje!" ));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>() );

        return userDetails;
    }
}
