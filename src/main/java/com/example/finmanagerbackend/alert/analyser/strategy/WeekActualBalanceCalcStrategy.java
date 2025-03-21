package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.financial_transaction.FinancialTransactionService;
import com.example.finmanagerbackend.limit.Limit;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy class for calculating the actual balance of a specific week.
 */
@RequiredArgsConstructor
public class WeekActualBalanceCalcStrategy implements ActualBalanceCalcStrategy {

    private final FinancialTransactionService financialTransactionService;

    // Method to calculate the actual balance of the week based on the provided limit.
    @Override
    public Double calcActualBalanceOfPeriod( Limit limit ) {

        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();

        return financialTransactionService.getWeekExpenses(
                firstLastWeekDay.get(0),
                firstLastWeekDay.get(1)
        );
    }

    // Method to get the start and end dates of the current week.
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
