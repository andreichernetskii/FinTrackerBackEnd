package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.example.finmanagerbackend.limit.ILimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import com.github.javafaker.Bool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    // checking is limits are exceeded

    public Map<String, Boolean> checkLimitsStats(  ) {
        Map<String, Boolean> limitsStats = createControllingMap();
        limitsStats.put( "Negative account condition", isNegativeConditionOfAccount() );

        return limitsStats;
    }

    private Map<String, Boolean> createControllingMap() {
        Map<String, Boolean> controllingMap = new HashMap<>();
        controllingMap.put( "Negative account condition", false );
        controllingMap.put( "More than prev month", false );
        controllingMap.put( "Budget exceeded", false );
        controllingMap.put( "Year limit exceeded", false );
        controllingMap.put( "Month limit exceeded", false );
        controllingMap.put( "Week limit exceeded", false );
        controllingMap.put( "Day limit exceeded", false );

        return controllingMap;
    }

    private boolean isNegativeConditionOfAccount() {
        Double actualBalance = incomeExpenseRepository.calculateAnnualBalance();
        if ( actualBalance < 0 ) return true;
        return false;
    }


}
