package com.example.finmanagerbackend.income_expense;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/operations")
    public List<IncomeExpense> getOperations() {
        List<IncomeExpense> list = incomeExpenseService.getOperations();
        return list;
    }

    @DeleteMapping("/operations/{operationId}")
    public void deleteIncomeExpense(@PathVariable("operationId") Long operationId) {
        incomeExpenseService.deleteIncomeExpense(operationId);
    }

    @GetMapping("/operations/annual") // todo: refactor
    public Map<String, BigDecimal> getAnnualBalance() {
        return incomeExpenseService.getAnnualBalance();
    }
}
