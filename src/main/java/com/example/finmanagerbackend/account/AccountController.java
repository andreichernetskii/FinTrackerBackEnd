package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.income_expense.IncomeExpense;
import com.example.finmanagerbackend.income_expense.IncomeExpenseController;
import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import com.example.finmanagerbackend.limit.LimitController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/accounts" )
public class AccountController {
    private final AccountService accountService;
    private final IncomeExpenseController incomeExpenseController;
    private final LimitController limitController;

    public AccountController( AccountService accountService,
                              IncomeExpenseController incomeExpenseController,
                              LimitController limitController ) {

        this.accountService = accountService;
        this.incomeExpenseController = incomeExpenseController;
        this.limitController = limitController;
    }

    @PostMapping( "/operations" )
    public void addNewOperation( HttpServletRequest request,
                                 @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {

        Account account = accountService.getAccountFromRequest( request );
        incomeExpenseController.addNewIncomeExpense( account, incomeExpenseDTO );
    }

    @PutMapping( "/operations" )
    public void updateOperation( HttpServletRequest request,
                                 @RequestBody IncomeExpense incomeExpense ) {

        Account account = accountService.getAccountFromRequest( request );
        incomeExpenseController.updateIncomeExpense( account, incomeExpense );
    }

    @DeleteMapping( "/operations/{operationId}" )
    public void deleteOperation( HttpServletRequest request,
                                 @PathVariable( "operationId" ) Long operationId ) {

        Account account = accountService.getAccountFromRequest( request );
        incomeExpenseController.deleteIncomeExpense( account, operationId );
    }
}
