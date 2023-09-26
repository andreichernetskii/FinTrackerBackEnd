package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final LimitRepository limitRepository;

    public AlertService( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> showAllAlerts() {
        FinAnalyser finAnalyser = new FinAnalyser( incomeExpenseRepository, limitRepository );
        Map<String, BigDecimal> actualLimits = finAnalyser.getActualLimits();

        List<AlertDTO> alerts = new ArrayList<>();

        if ( finAnalyser.isNegativeConditionOfAccount() ) {
            alerts.add( new AlertDTO( AlertType.NEGATIVE_BALANCE.label, false ) );
        }

        if ( finAnalyser.isYearLimitExceeded( actualLimits.get( "year" ) ) ) {
            alerts.add( new AlertDTO( AlertType.YEAR_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isMonthLimitExceeded( actualLimits.get( "month" ) ) ) {
            alerts.add( new AlertDTO( AlertType.MONTH_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isWeekLimitExceeded( actualLimits.get( "week" ) ) ) {
            alerts.add( new AlertDTO( AlertType.WEEK_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isDayLimitExceeded( actualLimits.get( "day" ) ) ) {
            alerts.add( new AlertDTO( AlertType.DAY_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isBudgetExceeded( actualLimits.get( "budget" ) ) ) {
            alerts.add( new AlertDTO( AlertType.BUDGET_LIMIT_EXCEEDING.label, false ) );
        }

        return alerts;
    }
}
