package com.example.finmanagerbackend.income_expense;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Api Controller
@RestController
@RequestMapping( path = "api/v1/operations" )
public class IncomeExpenseController {
    private final IncomeExpenseService incomeExpenseService;

    public IncomeExpenseController( IncomeExpenseService incomeExpenseService ) {
        this.incomeExpenseService = incomeExpenseService;
    }

    @PostMapping( "/" )
    public void addNewIncomeExpense( @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {
        incomeExpenseService.addIncomeExpense( incomeExpenseDTO );
    }

    @PutMapping( "/" )
    public void updateIncomeExpense( @RequestBody IncomeExpense incomeExpense ) {
        incomeExpenseService.updateIncomeExpense( incomeExpense );
    }

    @DeleteMapping( "/{operationId}" )
    public void deleteIncomeExpense( @PathVariable( "operationId" ) Long operationId ) {
        incomeExpenseService.deleteIncomeExpense( operationId );
    }

    @GetMapping( "/" )
    public List<IncomeExpense> getOperationsOfPeriod( @RequestParam( name = "year", required = false ) Integer year,
                                                      @RequestParam( name = "month", required = false ) Integer month,
                                                      @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                                      @RequestParam( name = "category", required = false ) String category ) {

        List<IncomeExpense> list = incomeExpenseService.getOperationsByCriteria( year, month, operationType, category );
        return list;
    }

    @GetMapping( "/annual" )
    public Double getAnnualBalance( @RequestParam( name = "year", required = false ) Integer year,
                                      @RequestParam( name = "month", required = false ) Integer month,
                                      @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                      @RequestParam( name = "category", required = false ) String category ) {

        Double totalAmount = incomeExpenseService.getAnnualBalance( year, month, operationType, category );
        return totalAmount;
    }

    @GetMapping( "/categories" )
    public List<String> getCategories() {
        List<String> categories = incomeExpenseService.getCategories();
        return categories;
    }
}
