package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.limit.LimitService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final IncomeExpenseService incomeExpenseService;
    private final LimitService limitService;
    private final ApplicationUserRepository applicationUserRepository;
    private final AccountRepository accountRepository;

    public AccountService( IncomeExpenseService incomeExpenseService,
                           LimitService limitService,
                           ApplicationUserRepository applicationUserRepository, AccountRepository accountRepository ) {

        this.incomeExpenseService = incomeExpenseService;
        this.limitService = limitService;
        this.applicationUserRepository = applicationUserRepository;
        this.accountRepository = accountRepository;
    }

    // musi być account, na którym operujemy
    // account musi być wzięty z appUser chyba
    public void addNewOperation( Long accountId, IncomeExpenseDTO incomeExpenseDTO ) {
//        Optional<ApplicationUser> userOptional = applicationUserRepository.findByEmail( userId );
////        Account account;
////
////        if ( userOptional.isPresent() ) {
////            account = userOptional.get().getAccount();
////            incomeExpenseService.addIncomeExpense( account.getId(), incomeExpenseDTO );
////        } else {
////            System.out.println( "account not found" );
////        }

        Optional<Account> optionalAccount = accountRepository.findById( accountId );
        if ( optionalAccount.isPresent() ) {
            Account account = optionalAccount.get();
            incomeExpenseService.addIncomeExpense( account, incomeExpenseDTO );
        }
    }
}
