package com.example.finmanagerbackend.alert.analyser;

import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.alert.analyser.strategy.ActualBalanceCalcStrategy;
import com.example.finmanagerbackend.alert.analyser.strategy.DayActualBalanceCalcStrategy;
import com.example.finmanagerbackend.alert.analyser.strategy.MonthActualBalanceCalcStrategy;
import com.example.finmanagerbackend.alert.analyser.strategy.NegativeActualStatusCalcStrategy;
import com.example.finmanagerbackend.alert.analyser.strategy.WeekActualBalanceCalcStrategy;
import com.example.finmanagerbackend.alert.analyser.strategy.YearActualBalanceCalcStrategy;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionService;
import com.example.finmanagerbackend.limit.LimitDTO;
import com.example.finmanagerbackend.limit.LimitService;
import com.example.finmanagerbackend.limit.LimitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for calculating financial statistics and generating alerts.
 */
@RequiredArgsConstructor
@Service
public class FinAnalyser {

    private final FinancialTransactionService financialTransactionService;
    private final LimitService limitService;
    private ActualBalanceCalcStrategy strategy;

    // Method to create alerts based on limits and financial statistics.
    public List<AlertDTO> createAlerts() {

        List<LimitDTO> limitsList = limitService.getLimits();
        List<AlertDTO> alerts = new ArrayList<>();

        // Iterate through all limits to check if they have been exceeded
        for ( LimitDTO limit : limitsList ) {
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

    // Method to calculate the discrepancy between the limit and actual balance.
    private BigDecimal calcDiscrepancy( LimitDTO limit ) {

        setStrategy( limit.getLimitType() );

        Double actualBalanceOfLimitPeriod = calcActualLimitOfLimitPeriod( limit );

        BigDecimal actualBalance = ( actualBalanceOfLimitPeriod != null )
                ? BigDecimal.valueOf( actualBalanceOfLimitPeriod ).abs()
                : new BigDecimal( 0 );

        return actualBalance.subtract( limit.getLimitAmount() );
    }

    // Method to set the strategy for calculating actual balance based on the limit type.
    private void setStrategy( LimitType limitType ) {

        strategy = switch ( limitType ) {
            case ZERO -> new NegativeActualStatusCalcStrategy( financialTransactionService );
            case YEAR -> new YearActualBalanceCalcStrategy( financialTransactionService );
            case MONTH -> new MonthActualBalanceCalcStrategy( financialTransactionService );
            case WEEK -> new WeekActualBalanceCalcStrategy( financialTransactionService );
            case DAY -> new DayActualBalanceCalcStrategy( financialTransactionService );
            default -> throw new IllegalStateException();
        };
    }

    // Method to calculate the actual balance of the limit period using the selected strategy.
    private Double calcActualLimitOfLimitPeriod( LimitDTO limit ) {
        return strategy.calcActualBalanceOfPeriod( limit );
    }

    // Method to generate an alert message based on the limit and discrepancy.
    public String generateAlertMessage( LimitDTO limit, BigDecimal discrepancy ) {
        return "!!! " + limit.getLimitType().getAlert()
                + " Limit of " + limit.getLimitAmount()
                + " has been exceeded by " + discrepancy.toPlainString();
    }
}
