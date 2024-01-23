package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific year.
 */
public class YearActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final FinancialTransactionRepository financialTransactionRepository;

    public YearActualBalanceCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // Method to calculate the actual balance of the year based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionRepository.calculateYearExpenses( LocalDate.now() );
    }
}
