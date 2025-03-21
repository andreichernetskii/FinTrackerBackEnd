package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.alert.analyser.FinAnalyser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing and displaying alerts based on financial analysis.
 */
@RequiredArgsConstructor
@Service
public class AlertService {

    private final FinAnalyser finAnalyser;

    // Method to retrieve and display all alerts using the FinAnalyser.
    public List<AlertDTO> showAllAlerts() {

        return finAnalyser.createAlerts();
    }
}
