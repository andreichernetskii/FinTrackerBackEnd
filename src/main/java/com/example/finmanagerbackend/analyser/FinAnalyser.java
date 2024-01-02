package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy.*;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service class responsible for calculating financial statistics and generating alerts.
 */
@Service
public class FinAnalyser {
    private FinancialTransactionRepository financialTransactionRepository;
    private LimitRepository limitRepository;
    private ActualBalanceCalcStrategy strategy;

    public FinAnalyser( FinancialTransactionRepository financialTransactionRepository, LimitRepository limitRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
        this.limitRepository = limitRepository;
    }

    // Method to create alerts based on limits and financial statistics.
    public List<AlertDTO> createAlerts() {
        List<Limit> limitsList = limitRepository.findAll();
        List<AlertDTO> alerts = new ArrayList<>();

        // Iterate through all limits to check if they have been exceeded
        for ( Limit limit : limitsList ) {
            if ( limit.getLimitAmount() == null ) continue;

            // Calculate the discrepancy between the limit and actual balance
            BigDecimal discrepancy = calcDiscrepancy( limit );

            // If the discrepancy is positive, create a new alert message
            if ( discrepancy.compareTo( BigDecimal.ZERO ) > 0 ) {
                alerts.add( new AlertDTO( generateAlertMessage( limit, discrepancy ) ) );
            }
        }

        return alerts;
    }

    // Method to set the strategy for calculating actual balance based on the limit type.
    private void setStrategy( LimitType limitType ) {
        strategy = switch ( limitType ) {
            case ZERO -> new NegativeActualStatusCalcStrategy( financialTransactionRepository );
            case YEAR -> new YearActualBalanceCalcStrategy( financialTransactionRepository );
            case MONTH -> new MonthActualBalanceCalcStrategy( financialTransactionRepository );
            case WEEK -> new WeekActualBalanceCalcStrategy( financialTransactionRepository );
            case DAY -> new DayActualBalanceCalcStrategy( financialTransactionRepository );
            default -> throw new IllegalStateException();
        };
    }

    // Method to calculate the discrepancy between the limit and actual balance.
    private BigDecimal calcDiscrepancy( Limit limit ) {
        setStrategy( limit.getLimitType() );

        Double actualBalanceOfLimitPeriod = calcActualLimitOfLimitPeriod( limit );

        BigDecimal actualBalance = ( actualBalanceOfLimitPeriod != null )
                ? BigDecimal.valueOf( actualBalanceOfLimitPeriod ).abs()
                : new BigDecimal( 0 );

        return actualBalance.subtract( limit.getLimitAmount() );
    }

    // Method to calculate the actual balance of the limit period using the selected strategy.
    private Double calcActualLimitOfLimitPeriod( Limit limit ) {
        return strategy.calcActualBalanceOfPeriod( limit );
    }

    // Method to generate an alert message based on the limit and discrepancy.
    public String generateAlertMessage( Limit limit, BigDecimal discrepancy ) {
        return "!!! " + limit.getLimitType().getAlert()
                + " Limit o wartości " + limit.getLimitAmount()
                + " został przekrocony o " + discrepancy.toPlainString();
    }
}
