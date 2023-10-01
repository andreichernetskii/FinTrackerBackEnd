package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class YearLimitCalcStrategy implements LimitCalcStrategy {
    private IncomeExpenseRepository incomeExpenseRepository;

    public YearLimitCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @Override
    public boolean isLimitExceeded( Limit limit ) {
        if ( limit.getLimitAmount() == null ) return false;

        Double actualBalanceAsDouble = incomeExpenseRepository.calculateYearExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( limit.getLimitAmount() ) >= 0;
    }
}
