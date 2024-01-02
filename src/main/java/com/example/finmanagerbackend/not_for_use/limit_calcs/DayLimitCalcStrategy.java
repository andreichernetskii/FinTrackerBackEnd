package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DayLimitCalcStrategy implements LimitCalcStrategy {
    private FinancialTransactionRepository financialTransactionRepository;

    public DayLimitCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    @Override
    public boolean isLimitExceeded( Limit limit ) {
        if ( limit.getLimitAmount() == null ) return false;

        Double actualBalanceAsDouble = financialTransactionRepository.calculateMonthExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( limit.getLimitAmount() ) >= 0;
    }
}
