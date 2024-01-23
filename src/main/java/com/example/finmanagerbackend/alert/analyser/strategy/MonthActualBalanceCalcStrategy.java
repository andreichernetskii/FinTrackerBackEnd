package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific month.
 */
public class MonthActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final FinancialTransactionRepository financialTransactionRepository;

    public MonthActualBalanceCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // Method to calculate the actual balance of the month based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionRepository.calculateMonthExpenses( LocalDate.now() );
    }
}
