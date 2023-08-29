package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.example.finmanagerbackend.limit.ILimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import com.github.javafaker.Bool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public Boolean isMonthLimitExceeded() {
        if ( monthLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateAnnualBalanceByCriteria(
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                null,
                null );
        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble );
        int comparisonToZero = monthLimit.subtract( actualBalance.abs() ).compareTo( BigDecimal.ZERO );
        return comparisonToZero < 0;
    }

    public Boolean isYearLimitExceeded() {
        if ( yearLimit == null ) return false;
        Double actualBalanceAsDouble = incomeExpenseRepository.calculateAnnualBalanceByCriteria(
                LocalDate.now().getYear(),
                null,
                null,
                null );
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


}
