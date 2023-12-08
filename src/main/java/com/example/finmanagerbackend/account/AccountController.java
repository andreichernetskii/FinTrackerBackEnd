package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/account" )
public class AccountController {
    private final AccountService accountService;

    public AccountController( AccountService accountService ) {
        this.accountService = accountService;
    }

    @PostMapping( "/add-operation" )
    public void addNewOperation( HttpServletRequest request,
                                 @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {

        accountService.addNewOperation( request, incomeExpenseDTO );
    }
}
