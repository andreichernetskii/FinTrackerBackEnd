package com.example.finmanagerbackend.application_user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;

    public UserDetailsServiceImpl( ApplicationUserRepository applicationUserRepository ) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserRepository.findByEmail( username )
                .orElseThrow( () -> new UsernameNotFoundException( String.format( "User with email %s not found", username ) ) );

        return UserDetailsImpl.build( user );
    }
}
