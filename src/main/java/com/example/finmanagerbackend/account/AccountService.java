package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.income_expense.IncomeExpense;
import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.limit.LimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final IncomeExpenseService incomeExpenseService;
    private final LimitService limitService;
    private final ApplicationUserRepository applicationUserRepository;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;

    public AccountService( IncomeExpenseService incomeExpenseService,
                           LimitService limitService,
                           ApplicationUserRepository applicationUserRepository,
                           AccountRepository accountRepository,
                           JwtUtils jwtUtils ) {

        this.incomeExpenseService = incomeExpenseService;
        this.limitService = limitService;
        this.applicationUserRepository = applicationUserRepository;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
    }

    // musi być account, na którym operujemy
    // account musi być wzięty z appUser chyba
    public void addNewOperation( HttpServletRequest request, IncomeExpenseDTO incomeExpenseDTO ) {
        Account account = getAccountFromRequest( request );

        if ( account != null ) {
            incomeExpenseService.addIncomeExpense( account, incomeExpenseDTO );
        } else {
            // todo: przerobić na jakiś porządny komunikat albo coś jeszcze
            System.out.println( "account not exist" );
        }
    }

    public void updateOperation( HttpServletRequest request, IncomeExpense incomeExpense ) {
        Account account = getAccountFromRequest( request );

        if ( account != null ) {
            incomeExpenseService.updateIncomeExpense( account, incomeExpense );
        } else {
            // todo: przerobić na jakiś porządny komunikat albo coś jeszcze
            System.out.println( "account not exist" );
        }
    }

    public Account getAccountFromRequest( HttpServletRequest request ) {
        String jwt = jwtUtils.parseJwt( request );
        String userEmail = jwtUtils.getUserNameFromJwtToken( jwt );
        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail( userEmail );

        return optionalUser.map( ApplicationUser::getAccount ).orElse( null );
    }
}
