package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import com.example.finmanagerbackend.limit.ILimitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    private final IIncomeExpenseRepository incomeExpenseRepository;
    private final ILimitRepository limitRepository;

    public AlertService( IIncomeExpenseRepository incomeExpenseRepository, ILimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> showAllAlerts() {
        FinAnalyser finAnalyser = new FinAnalyser( incomeExpenseRepository, limitRepository );
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
