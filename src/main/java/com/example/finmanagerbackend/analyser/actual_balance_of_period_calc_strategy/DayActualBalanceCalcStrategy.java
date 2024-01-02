package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific day.
 */
public class DayActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final FinancialTransactionRepository financialTransactionRepository;

    public DayActualBalanceCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // Method to calculate the actual balance of the day based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionRepository.calculateDayExpenses( LocalDate.now() );
    }
}
