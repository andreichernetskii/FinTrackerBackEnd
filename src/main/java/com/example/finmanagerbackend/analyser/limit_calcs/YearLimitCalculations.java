package com.example.finmanagerbackend.analyser.limit_calcs;

import java.math.BigDecimal;

public class YearLimitCalculations extends LimitCalculations {
    public YearLimitCalculations() {
        this.limitCalcStrategy = new YearLimitCalcStrategy( incomeExpenseRepository );
    }
    @Override
    protected BigDecimal calculateDiscrepancy() {
        return discrepancy;
    }
}
