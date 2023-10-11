package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public WeekActualBalanceCalcStrategy( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {
        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();

        return incomeExpenseRepository.calculateWeekExpenses(
                firstLastWeekDay.get( 0 ),
                firstLastWeekDay.get( 1 )
        );
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
