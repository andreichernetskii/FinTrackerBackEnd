package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.github.javafaker.Bool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<String, Boolean> limitsStats = finAnalyser.checkLimitsStats();
//         default limitsStats stats:
//        "Negative account condition", false
//        "More than prev month", false
//        "Budget exceeded", false
//        "Year limit exceeded", false
//        "Month limit exceeded", false
//        "Week limit exceeded", false
//        "Day limit exceeded", false

        if ( limitsStats.get( "Negative account condition" ) ) {
            alerts.add( new AlertDTO( AlertType.NEGATIVE_BALANCE.label, false ) );
        }

        if ( limitsStats.get( "Budget exceeded" ) ) {
            alerts.add( new AlertDTO( AlertType.BUDGET_LIMIT_EXCEEDING.label, false ) );
        }

        if ( limitsStats.get( "Year limit exceeded" ) ) {
            alerts.add( new AlertDTO( AlertType.YEAR_LIMIT_EXCEEDING.label, false ) );
        }

        if ( limitsStats.get( "Month limit exceeded" ) ) {
            alerts.add( new AlertDTO( AlertType.MONTH_LIMIT_EXCEEDING.label, false ) );
        }

        return alerts;
    }
}
