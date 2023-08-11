package com.example.finmanagerbackend.income_expense;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Api Controller
@RestController
@RequestMapping(path = "api/v1/incomes-expenses")
public class IncomeExpenseController {
    private IncomeExpenseService incomeExpenseService;

    public IncomeExpenseController(IncomeExpenseService incomeExpenseService) {
        this.incomeExpenseService = incomeExpenseService;
    }

    @PostMapping
    public void addNewIncomeExpense(@RequestBody IncomeExpenseDTO incomeExpenseDTO) {
        incomeExpenseService.addIncomeExpense(incomeExpenseDTO);
    }

    @PutMapping("/operations/update-operation")
    public void updateIncomeExpense(@RequestBody IncomeExpense incomeExpense) {
        incomeExpenseService.updateIncomeExpense(incomeExpense);
    }

    @GetMapping("/operations")
    public List<IncomeExpense> getOperations() {
        List<IncomeExpense> list = incomeExpenseService.getOperations();
        return list;
    }

    @DeleteMapping("/operations/{operationId}")
    public void deleteIncomeExpense( @PathVariable("operationId") Long operationId ) {
        incomeExpenseService.deleteIncomeExpense(operationId);
    }

//    @GetMapping("/operations/annual") // todo: refactor
//    public Map<String, BigDecimal> getAnnualBalance() {
//        return incomeExpenseService.getAnnualBalance();
//    }

    @GetMapping("/operations/statistics")
    public List<IncomeExpense> getOperationsOfPeriod(@RequestParam( name = "year", required = false ) Integer year,
                                                     @RequestParam( name = "month", required = false ) Integer month,
                                                     @RequestParam( name = "operationType", required = false ) OperationType operationType) {
        List<IncomeExpense> list;
        list = incomeExpenseService.getOperationsByCriteria(year, month, operationType);
        return list;
    }
}
