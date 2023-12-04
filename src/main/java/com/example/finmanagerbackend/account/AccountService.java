package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.limit.LimitService;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final IncomeExpenseService incomeExpenseService;
    private final LimitService limitService;

    public AccountService(IncomeExpenseService incomeExpenseService, LimitService limitService) {
        this.incomeExpenseService = incomeExpenseService;
        this.limitService = limitService;
    }
}
