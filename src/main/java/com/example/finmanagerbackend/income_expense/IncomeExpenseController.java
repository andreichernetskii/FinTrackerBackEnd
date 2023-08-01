package com.example.finmanagerbackend.income_expense;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/operations")
    public List<IncomeExpense> getOperations() {
        List<IncomeExpense> list = incomeExpenseService.getOperations();
        return list;
    }

    @DeleteMapping("/operations/{operationId}")
    public void deleteIncomeExpense(@PathVariable("operationId") Long operationId) {
        incomeExpenseService.deleteIncomeExpense(operationId);
    }
}
