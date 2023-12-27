package com.example.finmanagerbackend.income_expense;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Api Controller: This class serves as a controller for handling income and expense operations through API endpoints.
 */
@RestController
@RequestMapping( path = "api/v1/operations" )
public class IncomeExpenseController {
    private final IncomeExpenseService incomeExpenseService;

    // Constructor: Initializes the controller with an instance of IncomeExpenseService.
    public IncomeExpenseController( IncomeExpenseService incomeExpenseService ) {
        this.incomeExpenseService = incomeExpenseService;
    }

    // Endpoint to add a new income or expense operation.
    @PostMapping( "/" )
    public void addNewIncomeExpense( @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {

        incomeExpenseService.addIncomeExpense( incomeExpenseDTO );
    }

    // Endpoint to update an existing income or expense operation.
    @PutMapping( "/" )
    public void updateIncomeExpense( @RequestBody IncomeExpense incomeExpense ) {

        incomeExpenseService.updateIncomeExpense( incomeExpense );
    }

    // Endpoint to delete an income or expense operation based on the operationId.
    @DeleteMapping( "/{operationId}" )
    public void deleteIncomeExpense( @PathVariable( "operationId" ) Long operationId ) {
        incomeExpenseService.deleteIncomeExpense( operationId );
    }

    // Endpoint to retrieve a list of income and expense operations based on specified criteria.
    @GetMapping( "/" )
    public List<IncomeExpense> getOperationsOfPeriod( @RequestParam( name = "year", required = false ) Integer year,
                                                      @RequestParam( name = "month", required = false ) Integer month,
                                                      @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                                      @RequestParam( name = "category", required = false ) String category ) {

        List<IncomeExpense> list = incomeExpenseService.getOperationsByCriteria(
                year,
                month,
                operationType,
                category );

        return list;
    }

    // Endpoint to retrieve the annual balance based on specified criteria.
    @GetMapping( "/annual" )
    public Double getAnnualBalance( @RequestParam( name = "year", required = false ) Integer year,
                                    @RequestParam( name = "month", required = false ) Integer month,
                                    @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                    @RequestParam( name = "category", required = false ) String category ) {

        Double totalAmount = incomeExpenseService.getAnnualBalance( year, month, operationType, category );
        return totalAmount;
    }

    // Endpoint to retrieve a list of categories.
    @GetMapping( "/categories" )
    public List<String> getCategories() {
        return incomeExpenseService.getCategories();
    }
}
