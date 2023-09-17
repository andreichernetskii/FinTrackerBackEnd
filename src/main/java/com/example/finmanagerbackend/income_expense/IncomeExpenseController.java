package com.example.finmanagerbackend.income_expense;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Api Controller
@RestController
@RequestMapping( path = "api/v1/incomes-expenses" )
public class IncomeExpenseController {
    private final IncomeExpenseService incomeExpenseService;

    public IncomeExpenseController( IncomeExpenseService incomeExpenseService ) {
        this.incomeExpenseService = incomeExpenseService;
    }

    @PostMapping
    public void addNewIncomeExpense( @RequestBody IncomeExpenseDTO incomeExpenseDTO ) {
        incomeExpenseService.addIncomeExpense( incomeExpenseDTO );
    }

    @PutMapping( "/operations/update-operation" )
    public void updateIncomeExpense( @RequestBody IncomeExpense incomeExpense ) {
        incomeExpenseService.updateIncomeExpense( incomeExpense );
    }

    @GetMapping( "/operations" )
    public List<IncomeExpense> getOperations() {
        List<IncomeExpense> list = incomeExpenseService.getOperations();
        return list;
    }

    @DeleteMapping( "/operations/{operationId}" )
    public void deleteIncomeExpense( @PathVariable( "operationId" ) Long operationId ) {
        incomeExpenseService.deleteIncomeExpense( operationId );
    }

    @GetMapping( "/operations/statistics" )
    public List<IncomeExpense> getOperationsOfPeriod( @RequestParam( name = "year", required = false ) Integer year,
                                                      @RequestParam( name = "month", required = false ) Integer month,
                                                      @RequestParam( name = "operationType", required = false ) OperationType operationType,
                                                      @RequestParam( name = "category", required = false ) String category ) {
        List<IncomeExpense> list = incomeExpenseService.getOperationsByCriteria( year, month, operationType, category );
        return list;
    }

    @GetMapping( "/operations/annual" )
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
