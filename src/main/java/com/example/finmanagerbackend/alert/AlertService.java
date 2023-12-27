package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing and displaying alerts based on financial analysis.
 */
@Service
public class AlertService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final LimitRepository limitRepository;

    public AlertService( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    // Method to retrieve and display all alerts using the FinAnalyser.
    public List<AlertDTO> showAllAlerts() {
        FinAnalyser finAnalyser = new FinAnalyser( incomeExpenseRepository, limitRepository );
        return finAnalyser.createAlerts();
    }
}
