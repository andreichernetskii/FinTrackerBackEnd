package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.example.finmanagerbackend.limit.ILimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinAnalyser {
    private BigDecimal budget;
    private BigDecimal yearLimit;
    private BigDecimal monthLimit;
    private BigDecimal weekLimit;
    private BigDecimal dayLimit;
    private IIncomeExpenseRepository incomeExpenseRepository;
    private ILimitRepository limitRepository;

    public FinAnalyser( IIncomeExpenseRepository incomeExpenseRepository, ILimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    private void setBudget() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.BUDGET );
        if ( val != null ) this.monthLimit = BigDecimal.valueOf( val );
    }

    private void setYearLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.YEAR );
        if ( val != null ) this.yearLimit = BigDecimal.valueOf( val );
    }

    private void setMonthLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.MONTH );
        if ( val != null ) this.monthLimit = BigDecimal.valueOf( val );
    }

    private void setWeekLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.WEEK );
        if ( val != null ) this.weekLimit = BigDecimal.valueOf( val );
    }

    private void setDayLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.DAY );
        if ( val != null ) this.dayLimit = BigDecimal.valueOf( val );
    }

    public void updateLimits() {
        setBudget();
        setYearLimit();
        setMonthLimit();
        setWeekLimit();
        setDayLimit();
    }

    // checking is limits are exceeded

    public Boolean isMonthLimitExceeded() {
        // todo: wymyślić, jak to można zrobić w jedną funkcję
        if ( monthLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateExpensesFromActualDate(
                LocalDate.now(),
                null,
                true,
                null,
                null,
                null);
        if (actualBalanceAsDouble == null) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = monthLimit.subtract( actualBalance.abs() ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    public Boolean isYearLimitExceeded() {
        if ( yearLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateExpensesFromActualDate(
                LocalDate.now(),
                true,
                null,
                null,
                null,
                null);
        if (actualBalanceAsDouble == null) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = yearLimit.subtract( actualBalance.abs() ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    public Boolean isBudgetExceeded() {
        if ( budget == null ) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( incomeExpenseRepository.calculateAnnualBalance() );
        int comparisonToZero = budget.subtract( actualBalance ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    public boolean isNegativeConditionOfAccount() {
        Double actualBalance = incomeExpenseRepository.calculateAnnualBalance();
        if ( actualBalance < 0 ) return true;
        return false;
    }

    public boolean isWeekLimitExceeded() {
        if ( weekLimit == null ) return false;
        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateExpensesFromActualDate(
                LocalDate.now(),
                null,
                null,
                firstLastWeekDay.get( 0 ),
                firstLastWeekDay.get( 1 ),
                null);
        if (actualBalanceAsDouble == null) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = weekLimit.subtract( actualBalance.abs() ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    public boolean isDayLimitExceeded() {
        if ( dayLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateExpensesFromActualDate(
                LocalDate.now(),
                null,
                null,
                null,
                null,
                true);
        if (actualBalanceAsDouble == null) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = dayLimit.subtract( actualBalance.abs() ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    private List<LocalDate> getStartAndEndOfWeekDates() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        dates.add( startOfWeek );
        dates.add( endOfWeek );

        return dates;
    }
}
