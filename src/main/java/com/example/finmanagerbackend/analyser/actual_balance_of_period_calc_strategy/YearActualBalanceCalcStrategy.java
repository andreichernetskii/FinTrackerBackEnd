package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific year.
 */
public class YearActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public YearActualBalanceCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    // Method to calculate the actual balance of the year based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return incomeExpenseRepository.calculateYearExpenses( LocalDate.now() );
    }
}
