package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.income_expense.IncomeExpense;
import com.example.finmanagerbackend.income_expense.IncomeExpenseController;
import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import com.example.finmanagerbackend.income_expense.OperationType;
import com.example.finmanagerbackend.limit.LimitController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping( "/operations" )
    public List<IncomeExpense> getOperations( HttpServletRequest request,
                                              @RequestParam( name = "year", required = false ) Integer year,
                                              @RequestParam( name = "month", required = false ) Integer month,
                                              @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                              @RequestParam( name = "category", required = false ) String category ) {

        Account account = accountService.getAccountFromRequest( request );
        List<IncomeExpense> list = incomeExpenseController.getOperationsOfPeriod(
                account,
                year,
                month,
                operationType,
                category
        );

        return list;
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
