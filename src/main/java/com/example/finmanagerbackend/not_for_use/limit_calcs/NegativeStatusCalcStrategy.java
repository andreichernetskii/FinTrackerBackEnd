package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

public class NegativeStatusCalcStrategy implements LimitCalcStrategy {
    private FinancialTransactionRepository financialTransactionRepository;
    public NegativeStatusCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // todo: zagłuszka
    // todo: limit dlaczego tutaj?
    @Override
    public boolean isLimitExceeded( Limit limit ) {
        return financialTransactionRepository.calculateAnnualBalance( null ) <0;
    }
}
