package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

public class DayActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public DayActualBalanceCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return incomeExpenseRepository.calculateDayExpenses( LocalDate.now() );
    }
}
