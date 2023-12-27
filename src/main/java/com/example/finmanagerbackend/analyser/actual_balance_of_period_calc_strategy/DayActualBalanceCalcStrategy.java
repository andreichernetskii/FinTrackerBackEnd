package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific day.
 */
public class DayActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public DayActualBalanceCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    // Method to calculate the actual balance of the day based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return incomeExpenseRepository.calculateDayExpenses( LocalDate.now() );
    }
}
