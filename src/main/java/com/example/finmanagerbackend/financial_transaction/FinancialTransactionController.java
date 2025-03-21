package com.example.finmanagerbackend.financial_transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Api Controller: This class serves as a controller for handling income and expense operations through API endpoints.
 */
@RestController
@RequestMapping( path = "api/v1/transactions" )
public class FinancialTransactionController {
    private final FinancialTransactionService financialTransactionService;

    // Constructor: Initializes the controller with an instance of IncomeExpenseService.
    public FinancialTransactionController( FinancialTransactionService financialTransactionService ) {
        this.financialTransactionService = financialTransactionService;
    }

    // Endpoint to add a new income or expense operation.
    @PostMapping( "/" )
    public ResponseEntity<?> addNewFinancialTransaction( @RequestBody FinancialTransactionDTO financialTransactionDTO ) {
        return financialTransactionService.addFinancialTransaction( financialTransactionDTO );
    }

    // Endpoint to update an existing income or expense operation.
    @PutMapping( "/" )
    public ResponseEntity<?> updateFinancialTransaction( @RequestBody FinancialTransaction financialTransaction ) {
        return financialTransactionService.updateFinancialTransaction( financialTransaction );
    }

    // Endpoint to delete an income or expense operation based on the operationId.
    @DeleteMapping( "/{operationId}" )
    public ResponseEntity<?> deleteFinancialTransaction( @PathVariable( "operationId" ) Long operationId ) {
        return financialTransactionService.deleteFinancialTransaction( operationId );
    }

    // Endpoint to retrieve a list of income and expense operations based on specified criteria.
    @GetMapping( "/" )
    public List<FinancialTransaction> getOperationsOfPeriod( @RequestParam( name = "year", required = false ) Integer year,
                                                             @RequestParam( name = "month", required = false ) Integer month,
                                                             @RequestParam( name = "financialTransactionType", required = false ) FinancialTransactionType financialTransactionType,
                                                             @RequestParam( name = "category", required = false ) String category ) {

            List<FinancialTransaction> result = financialTransactionService.getOperationsByCriteria(
                year,
                month,
                financialTransactionType,
                category );

        return result;
    }

    // Endpoint to retrieve the annual balance based on specified criteria.
    @GetMapping( "/annual" )
    public Double getAnnualBalance( @RequestParam( name = "year", required = false ) Integer year,
                                    @RequestParam( name = "month", required = false ) Integer month,
                                    @RequestParam( name = "financialTransactionType", required = false ) FinancialTransactionType financialTransactionType,
                                    @RequestParam( name = "category", required = false ) String category ) {

        return financialTransactionService.getAnnualBalance( year, month, financialTransactionType, category );
    }

    // Endpoint to retrieve a list of categories.
    @GetMapping( "/categories" )
    public List<String> getCategories() {
        return financialTransactionService.getCategories();
    }

    @GetMapping("/types")
    public List<String> getTransactionTypes() {
        return financialTransactionService.getTransactionTypes();
    }
}
