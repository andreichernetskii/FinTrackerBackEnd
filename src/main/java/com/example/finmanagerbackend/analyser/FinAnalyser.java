package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy.*;
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
    private ActualBalanceCalcStrategy strategy;

    public FinAnalyser( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> createAlerts() {
        List<Limit> limitsList = limitRepository.findAll();
        List<AlertDTO> alerts = new ArrayList<>();

        for ( Limit limit : limitsList ) {
            if ( limit.getLimitAmount() == null ) continue;

            // if limit was exceeded, his value of discrepancy will be added to a new alert message
            BigDecimal discrepancy = calcDiscrepancy( limit );

            if ( discrepancy.compareTo( BigDecimal.ZERO ) > 0 ) {
                // todo: ???
                alerts.add( new AlertDTO( generateAlertMessage( limit, discrepancy ), false ) );
            }
        }

        return alerts;
    }

    private void setStrategy( LimitType limitType ) {
        strategy = switch ( limitType ) {
            case ZERO -> new NegativeActualStatusCalcStrategy( incomeExpenseRepository );
            case YEAR -> new YearActualBalanceCalcStrategy( incomeExpenseRepository );
            case MONTH -> new MonthActualBalanceCalcStrategy( incomeExpenseRepository );
            case WEEK -> new WeekActualBalanceCalcStrategy( incomeExpenseRepository );
            case DAY -> new DayActualBalanceCalcStrategy( incomeExpenseRepository );
            default -> throw new IllegalStateException();
        };
    }

    private BigDecimal calcDiscrepancy( Limit limit ) {
        setStrategy( limit.getLimitType() );

        Double actualBalanceOfLimitPeriod = calcActualLimitOfLimitPeriod( limit );

        BigDecimal actualBalance = ( actualBalanceOfLimitPeriod != null )
                ? BigDecimal.valueOf( actualBalanceOfLimitPeriod ).abs()
                : new BigDecimal( 0 );

        return actualBalance.subtract( limit.getLimitAmount() );
    }

    private Double calcActualLimitOfLimitPeriod( Limit limit ) {
        return strategy.calcActualBalanceOfPeriod( limit );
    }

    public String generateAlertMessage( Limit limit, BigDecimal discrepancy ) {
        return "!!! " + limit.getLimitType().getAlert()
                + " Limit o wartości " + limit.getLimitAmount()
                + " został przekrocony o " + discrepancy.toPlainString();
    }


    // todo: zrobić budżet
    // todo: spróbować chain of responsibility
    // todo: przerobić jeszcze z  punktu widzenia, że są już limity i po kategoriach
}
