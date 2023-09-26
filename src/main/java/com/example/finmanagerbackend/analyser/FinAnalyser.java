package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinAnalyser {
    private IncomeExpenseRepository incomeExpenseRepository;
    private LimitRepository limitRepository;

    public FinAnalyser( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    private BigDecimal setBudget() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.BUDGET );
        return (val != null) ? BigDecimal.valueOf( val ) : null;
    }

    private BigDecimal setYearLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.YEAR );
        return (val != null) ? BigDecimal.valueOf( val ) : null;
    }

    private BigDecimal setMonthLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.MONTH );
        return (val != null) ? BigDecimal.valueOf( val ) : null;
    }

    private BigDecimal setWeekLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.WEEK );
        return (val != null) ? BigDecimal.valueOf( val ) : null;
    }

    private BigDecimal setDayLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.DAY );
        return (val != null) ? BigDecimal.valueOf( val ) : null;
    }

    public Map<String, BigDecimal> getActualLimits() {
        Map<String, BigDecimal> limits = new HashMap<>();
        limits.put( "budget", setBudget() );
        limits.put( "day", setDayLimit() );
        limits.put( "week", setWeekLimit() );
        limits.put( "month", setMonthLimit() );
        limits.put( "year", setYearLimit() );

        return limits;
    }

    // checking is limits are exceeded

    public Boolean isBudgetExceeded( BigDecimal budget) {
        if ( budget == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( incomeExpenseRepository.calculateAnnualBalance() ).abs();
        return actualBalance.compareTo( budget ) >= 0;
    }

    public boolean isDayLimitExceeded( BigDecimal dayLimit) {
        if ( dayLimit == null ) return false;

        Double actualBalanceAsDouble = incomeExpenseRepository.calculateDayExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( dayLimit ) >= 0;
    }

    public boolean isWeekLimitExceeded( BigDecimal weekLimit ) {
        if ( weekLimit == null ) return false;
        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();

        Double actualBalanceAsDouble = incomeExpenseRepository
                .calculateWeekExpenses( firstLastWeekDay.get( 0 ), firstLastWeekDay.get( 1 ) );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( weekLimit ) >= 0;
    }

    public Boolean isMonthLimitExceeded( BigDecimal monthLimit ) {
        if ( monthLimit == null ) return false;

        Double actualBalanceAsDouble = incomeExpenseRepository.calculateMonthExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( monthLimit ) >= 0;
    }

    public Boolean isYearLimitExceeded( BigDecimal yearLimit ) {
        if ( yearLimit == null ) return false;

        Double actualBalanceAsDouble = incomeExpenseRepository.calculateYearExpenses( LocalDate.now() );
        if ( actualBalanceAsDouble == null ) return false;

        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
        return actualBalance.compareTo( yearLimit ) >= 0;
    }

    public boolean isNegativeConditionOfAccount() {
        Double actualBalance = incomeExpenseRepository.calculateAnnualBalance();
        if ( actualBalance < 0 ) return true;

        return false;
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
