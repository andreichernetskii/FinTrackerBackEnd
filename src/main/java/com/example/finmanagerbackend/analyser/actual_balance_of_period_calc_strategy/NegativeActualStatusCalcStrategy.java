package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

public class NegativeActualStatusCalcStrategy implements ActualBalanceCalcStrategy {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public NegativeActualStatusCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    // todo: zagłuszka
    // todo: limit dlaczego tutaj?
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return incomeExpenseRepository.calculateAnnualBalance( null );
    }
}
