package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionService;
import com.example.finmanagerbackend.limit.Limit;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * Strategy class for calculating the actual balance of a specific day.
 */
@RequiredArgsConstructor
public class DayActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {

    private final FinancialTransactionService financialTransactionService;

    // Method to calculate the actual balance of the day based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionService.getDayExpenses(LocalDate.now());
    }
}
