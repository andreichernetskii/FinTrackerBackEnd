package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.alert.analyser.FinAnalyser;
import com.example.finmanagerbackend.financial_transaction.FinancialTransactionRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing and displaying alerts based on financial analysis.
 */
@RequiredArgsConstructor
@Service
public class AlertService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final LimitRepository limitRepository;
    private final AccountService accountService;

    // Method to retrieve and display all alerts using the FinAnalyser.
    public List<AlertDTO> showAllAlerts() {

        return new FinAnalyser(
                financialTransactionRepository,
                limitRepository,
                accountService )
                .createAlerts();
    }
}
