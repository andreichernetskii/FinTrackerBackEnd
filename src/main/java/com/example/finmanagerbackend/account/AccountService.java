package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.security.application_user.ApplicationUser;
import com.example.finmanagerbackend.security.application_user.AuthService;
import org.springframework.stereotype.Service;

/**
 * Service class for managing operations related to user accounts.
 */
@Service
public class AccountService {
    private final AuthService authService;

    public AccountService( AuthService authService ) {
        this.authService = authService;
    }


    public Account getAccount() {
        ApplicationUser user = authService.getLoggedUser();

        return user.getAccount();
    }
}
