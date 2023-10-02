package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FinAnalyser {
    private IncomeExpenseRepository incomeExpenseRepository;
    private LimitRepository limitRepository;
    private LimitCalcStrategy strategy;

    public FinAnalyser( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> calcActualLimits() {
        List<Limit> limitsList = limitRepository.findAll();
        List<AlertDTO> alerts = new ArrayList<>();

        for ( Limit limit : limitsList ) {
            setStrategy( limit.getLimitType() );
            if ( isLimitExceeded( limit ) )
                alerts.add( new AlertDTO( limit.getLimitType().getAlert(), false ) );
        }

        return alerts;
    }

    private void setStrategy( LimitType limitType ) {
        strategy = switch ( limitType ) {
            case YEAR -> new YearLimitCalcStrategy( incomeExpenseRepository );
            case MONTH -> new MonthLimitCalcStrategy( incomeExpenseRepository );
            case WEEK -> new WeekLimitCalcStrategy( incomeExpenseRepository );
            case DAY -> new DayLimitCalcStrategy( incomeExpenseRepository );
            case ZERO -> new NegativeStatusCalcStrategy( incomeExpenseRepository );
            default -> throw new IllegalStateException();
        };
    }

    private boolean isLimitExceeded( Limit limit ) {
        return strategy.isLimitExceeded( limit );
    }

    private BigDecimal getLimit( LimitType limitType ) {
        Double val = limitRepository.getLimitAmountByLimitType( limitType );
        return ( val != null ) ? BigDecimal.valueOf( val ) : null;
    }

    //todo: zrobić budżet
//    public boolean isNegativeConditionOfAccount() {
//        return incomeExpenseRepository.calculateAnnualBalance() <0;
//    }

    // todo sprobowac dodac klasy strAT jako klasy wewnt
    // todo: spróbować chain of responsibility
}
