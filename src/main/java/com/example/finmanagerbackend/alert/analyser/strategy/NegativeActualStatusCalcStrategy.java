package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionService;
import com.example.finmanagerbackend.limit.Limit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NegativeActualStatusCalcStrategy implements ActualBalanceCalcStrategy {

    private final FinancialTransactionService financialTransactionService;

    // todo: zagłuszka
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionService.getAnnualBalance(null, null, null, null);
    }
}
