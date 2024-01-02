package com.example.finmanagerbackend.not_for_use.limit_calcs;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekLimitCalcStrategy implements LimitCalcStrategy {
    private FinancialTransactionRepository financialTransactionRepository;

    public WeekLimitCalcStrategy( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    @Override
    public boolean isLimitExceeded( Limit limit ) {
        if ( limit.getLimitAmount() == null ) return false;
        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();

        Double actualBalanceAsDouble = financialTransactionRepository
                .calculateWeekExpenses( firstLastWeekDay.get( 0 ), firstLastWeekDay.get( 1 ) );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( limit.getLimitAmount() ) >= 0;
    }

    private List<LocalDate> getStartAndEndOfWeekDates() {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with( DayOfWeek.MONDAY ).minusDays( 1 );
        LocalDate sunday = today.with( DayOfWeek.SUNDAY ).minusDays( 1 );

        dates.add( monday );
        dates.add( sunday );

        return dates;
    }
}
