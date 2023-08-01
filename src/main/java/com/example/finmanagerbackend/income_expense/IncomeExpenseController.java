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

    // todo: przrrobić, żeby nie przeszkadzali sobie
    @PostMapping("/operations/{operationId}")
    public void updateIncomeExpense(
            @PathVariable("operationId") Long id,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) String category ) {
        incomeExpenseService.updateIncomeExpense(id, date, amount, category);
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
