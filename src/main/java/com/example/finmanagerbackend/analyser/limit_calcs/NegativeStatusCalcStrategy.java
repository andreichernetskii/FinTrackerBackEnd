package com.example.finmanagerbackend.analyser.limit_calcs;

import com.example.finmanagerbackend.analyser.limit_calcs.LimitCalcStrategy;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

public class NegativeStatusCalcStrategy implements LimitCalcStrategy {
    private IncomeExpenseRepository incomeExpenseRepository;
    public NegativeStatusCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }
    @Override
    public boolean isLimitExceeded( Limit limit ) {
        return incomeExpenseRepository.calculateAnnualBalance() <0;
    }
}
