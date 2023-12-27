package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.auth.AuthService;
import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.limit.LimitService;
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
