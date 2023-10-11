package com.example.finmanagerbackend.analyser.limit_calcs;

import com.example.finmanagerbackend.analyser.limit_calcs.LimitCalcStrategy;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthLimitCalcStrategy implements LimitCalcStrategy {
    private IncomeExpenseRepository incomeExpenseRepository;

    public MonthLimitCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @Override
    public boolean isLimitExceeded( Limit limit ) {
        if ( limit.getLimitAmount() == null ) return false;

        Double actualBalanceAsDouble = incomeExpenseRepository.calculateMonthExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( limit.getLimitAmount() ) >= 0;
    }
}
