package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/account" )
public class AccountController {
    private final AccountService accountService;

    public AccountController( AccountService accountService ) {
        this.accountService = accountService;
    }

    @PostMapping( "/{userId}" )
    public void addNewOperation( @PathVariable( "userId" ) Long userId,
                                 @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {

        accountService.addNewOperation( userId, incomeExpenseDTO );
    }

}
