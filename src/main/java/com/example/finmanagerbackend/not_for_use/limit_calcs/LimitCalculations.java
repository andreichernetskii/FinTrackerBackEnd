package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;

import java.math.BigDecimal;

public abstract class LimitCalculations {
    protected BigDecimal discrepancy;
    protected IncomeExpenseRepository incomeExpenseRepository;
    public LimitCalcStrategy limitCalcStrategy;
    protected abstract BigDecimal calculateDiscrepancy();

}
