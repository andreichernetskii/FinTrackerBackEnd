package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.example.finmanagerbackend.limit.ILimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import com.github.javafaker.Bool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinAnalyser {
    private BigDecimal budget;
    private BigDecimal yearLimit;
    private BigDecimal monthLimit;
    private BigDecimal weekLimit;
    private BigDecimal dayLimit;
    private LocalDate actualDate;

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
        if ( val != null ) this.monthLimit = BigDecimal.valueOf( val );
    }

    private void setDayLimit() {
        Double val = limitRepository.getLimitAmountByLimitType( LimitType.DAY );
        if ( val != null ) this.monthLimit = BigDecimal.valueOf( val );
    }

    public void updateLimits() {
        setBudget();
        setYearLimit();
        setMonthLimit();
        setWeekLimit();
        setDayLimit();
    }

    private void setActualDate() {
        actualDate = LocalDate.now();
    }

    // checking is limits are exceeded

    public Map<String, Boolean> checkLimitsStats() {
//        Map<String, Boolean> limitsStats = createControllingMap();
        Map<String, Boolean> limitsStats = new HashMap<>();
        limitsStats.put( "Negative account condition", isNegativeConditionOfAccount() );
        limitsStats.put( "More than prev month", false );
        limitsStats.put( "Budget exceeded", isBudgetExceeded() );
        limitsStats.put( "Year limit exceeded", isYearLimitExceeded() );
        limitsStats.put( "Month limit exceeded", isMonthLimitExceeded() );
        limitsStats.put( "Week limit exceeded", false );
        limitsStats.put( "Day limit exceeded", false );

        return limitsStats;
    }

    private Boolean isMonthLimitExceeded() {
        if ( monthLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateAnnualBalanceByCriteria(
                null,
                LocalDate.now().getMonthValue(),
                null,
                null );
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = monthLimit.subtract( actualBalance ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    private Boolean isYearLimitExceeded() {
        if ( yearLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateAnnualBalanceByCriteria(
                LocalDate.now().getYear(),
                null,
                null,
                null );
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = yearLimit.subtract( actualBalance ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    private Boolean isBudgetExceeded() {
        if ( budget == null ) return false;
        BigDecimal actualBalance = BigDecimal.valueOf( incomeExpenseRepository.calculateAnnualBalance() );
        int comparisonToZero = budget.subtract( actualBalance ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

//    private Map<String, Boolean> createControllingMap() {
//        Map<String, Boolean> controllingMap = new HashMap<>();
//        controllingMap.put( "Negative account condition", false );
//        controllingMap.put( "More than prev month", false );
//        controllingMap.put( "Budget exceeded", false );
//        controllingMap.put( "Year limit exceeded", false );
//        controllingMap.put( "Month limit exceeded", false );
//        controllingMap.put( "Week limit exceeded", false );
//        controllingMap.put( "Day limit exceeded", false );
//
//        return controllingMap;
//    }

    private boolean isNegativeConditionOfAccount() {
        Double actualBalance = incomeExpenseRepository.calculateAnnualBalance();
        if ( actualBalance < 0 ) return true;
        return false;
    }


}
