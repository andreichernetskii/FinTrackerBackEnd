package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

public class NegativeActualStatusCalcStrategy implements ActualBalanceCalcStrategy {
    private final FinancialTransactionRepository financialTransactionRepository;

    public NegativeActualStatusCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // todo: zagłuszka
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        return financialTransactionRepository.calculateAnnualBalance( null );
    }
}
