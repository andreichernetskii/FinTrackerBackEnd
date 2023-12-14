package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

public class NegativeStatusCalcStrategy implements LimitCalcStrategy {
    private IncomeExpenseRepository incomeExpenseRepository;
    public NegativeStatusCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    // todo: zagłuszka
    // todo: limit dlaczego tutaj?
    @Override
    public boolean isLimitExceeded( Limit limit ) {
        return incomeExpenseRepository.calculateAnnualBalance( null ) <0;
    }
}
