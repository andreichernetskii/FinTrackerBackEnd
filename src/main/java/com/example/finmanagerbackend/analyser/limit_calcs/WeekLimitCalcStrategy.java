package com.example.finmanagerbackend.analyser.limit_calcs;

import com.example.finmanagerbackend.analyser.limit_calcs.LimitCalcStrategy;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekLimitCalcStrategy implements LimitCalcStrategy {
    private IncomeExpenseRepository incomeExpenseRepository;

    public WeekLimitCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @Override
    public boolean isLimitExceeded( Limit limit ) {
        if ( limit.getLimitAmount() == null ) return false;
        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();

        Double actualBalanceAsDouble = incomeExpenseRepository
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
