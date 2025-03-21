package com.example.finmanagerbackend.alert.analyser;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.alert.analyser.strategy.*;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionService;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service class responsible for calculating financial statistics and generating alerts.
 */
@RequiredArgsConstructor
@Service
public class FinAnalyser {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final FinancialTransactionService financialTransactionService;
    private final LimitRepository limitRepository;
    private final AccountService accountService;
    private ActualBalanceCalcStrategy strategy;

    // Method to create alerts based on limits and financial statistics.
    public List<AlertDTO> createAlerts() {

        Account account = accountService.getAccount();
        List<Limit> limitsList = limitRepository.getAllLimitsWithoutZero( account.getId() );
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

    // Method to calculate the discrepancy between the limit and actual balance.
    private BigDecimal calcDiscrepancy( Limit limit ) {

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
    private Double calcActualLimitOfLimitPeriod( Limit limit ) {
        return strategy.calcActualBalanceOfPeriod( limit );
    }

    // Method to generate an alert message based on the limit and discrepancy.
    public String generateAlertMessage( Limit limit, BigDecimal discrepancy ) {
        return "!!! " + limit.getLimitType().getAlert()
                + " Limit of " + limit.getLimitAmount()
                + " has been exceeded by " + discrepancy.toPlainString();
    }
}
