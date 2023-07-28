package com.example.finmanagerbackend.income_expense;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public List<IncomeExpenseManager> getOperations() {
        System.out.println("Request got. Sending ...");
        List<IncomeExpenseManager> list = incomeExpenseService.getOperations();
        list.forEach(element -> System.out.println(element));
        return list;
    }
}
