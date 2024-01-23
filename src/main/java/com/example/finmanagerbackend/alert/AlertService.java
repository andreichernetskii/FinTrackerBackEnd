package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.alert.analyser.FinAnalyser;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing and displaying alerts based on financial analysis.
 */
@Service
public class AlertService {
    private final FinancialTransactionRepository financialTransactionRepository;
    private final LimitRepository limitRepository;
    private final AccountService accountService;

    public AlertService( FinancialTransactionRepository financialTransactionRepository, LimitRepository limitRepository, AccountService accountService ) {
        this.financialTransactionRepository = financialTransactionRepository;
        this.limitRepository = limitRepository;
        this.accountService = accountService;
    }

    // Method to retrieve and display all alerts using the FinAnalyser.
    public List<AlertDTO> showAllAlerts() {
        FinAnalyser finAnalyser = new FinAnalyser( financialTransactionRepository, limitRepository, accountService );
        return finAnalyser.createAlerts();
    }
}
