package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    private final IIncomeExpenseRepository iIncomeExpenseRepository;
    private final FinAnalyser finAnalyser;

    public AlertService( IIncomeExpenseRepository iIncomeExpenseRepository, FinAnalyser finAnalyser ) {
        this.iIncomeExpenseRepository = iIncomeExpenseRepository;
        this.finAnalyser = finAnalyser;
    }

    public List<AlertDTO> showAllAlerts() {
        finAnalyser.updateLimits();
        List<AlertDTO> alerts = new ArrayList<>();

        if ( finAnalyser.isNegativeConditionOfAccount() ) {
            alerts.add( new AlertDTO( AlertType.NEGATIVE_BALANCE.label, false ) );
        }

        if ( finAnalyser.isYearLimitExceeded() ) {
            alerts.add( new AlertDTO( AlertType.YEAR_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isMonthLimitExceeded() ) {
            alerts.add( new AlertDTO( AlertType.MONTH_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isWeekLimitExceeded() ) {
            alerts.add( new AlertDTO( AlertType.WEEK_LIMIT_EXCEEDING.label, false) );
        }

        if ( finAnalyser.isDayLimitExceeded() ) {
            alerts.add( new AlertDTO( AlertType.DAY_LIMIT_EXCEEDING.label, false) );
        }

        return alerts;
    }
}
