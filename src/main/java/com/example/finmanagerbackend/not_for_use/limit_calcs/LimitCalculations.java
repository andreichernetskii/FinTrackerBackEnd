package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;

import java.math.BigDecimal;

public abstract class LimitCalculations {
    protected BigDecimal discrepancy;
    protected FinancialTransactionRepository financialTransactionRepository;
    public LimitCalcStrategy limitCalcStrategy;
    protected abstract BigDecimal calculateDiscrepancy();

}
