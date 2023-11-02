package com.example.finmanagerbackend.application_user;

import com.example.finmanagerbackend.not_for_use.ApplicationUserRoleNotUse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passEncoder;

    public ApplicationUserService( ApplicationUserRepository applicationUserRepository, PasswordEncoder passEncoder ) {
        this.applicationUserRepository = applicationUserRepository;
        this.passEncoder = passEncoder;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findById( username )
                .orElseThrow(() -> new UsernameNotFoundException( "User nie istnieje!" ));
        UserDetails userDetails = new User(
                applicationUser.getUsername(), applicationUser.getPassword(), new ArrayList<>() );

        return userDetails;
    }

    public void newUser( ApplicationUserDTO applicationUserDTO ) {
        applicationUserRepository.save( new ApplicationUser(
                applicationUserDTO.getEmail(),
                passEncoder.encode( applicationUserDTO.getPassword()),
                ApplicationUserRoleNotUse.APP_USER.getGrantedAuthorities(),
                true,
                true,
                true,
                true )
        );
    }

    public List<ApplicationUser> showUsers() {
        return applicationUserRepository.findAll();
    }
}
