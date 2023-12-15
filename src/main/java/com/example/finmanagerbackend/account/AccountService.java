package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.auth.AuthService;
import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.jwt.JwtUtils;
import com.example.finmanagerbackend.limit.LimitService;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AuthService authService;

    public AccountService( AuthService authService ) {

        this.authService = authService;
    }

    // musi być account, na którym operujemy
    // account musi być wzięty z appUser chyba
    /*public void addNewOperation( HttpServletRequest request, IncomeExpenseDTO incomeExpenseDTO ) {
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
    }*/

    public Account getAccount( /*HttpServletRequest request*/ ) {
        /* Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails) ) {
            throw new RuntimeException("User not logged");
        }
        UserDetails userDetails = ( UserDetails ) principal;
        accountRepository.findAccountByEmail( userDetails.getUsername() ); */

        ApplicationUser user = authService.getLoggedUser();
        return user.getAccount();


//        String jwt = jwtUtils.parseJwt( request );
//        String userEmail = jwtUtils.getUserNameFromJwtToken( jwt );
//        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail( userEmail );
//
//        return optionalUser.map( ApplicationUser::getAccount ).orElse( null );
    }
}
