package com.example.finmanagerbackend.alert;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller responsible for handling alerts related to user's financials.
 */
@RestController
@RequestMapping( "/api/v1/alerts" )
public class AlertController {
    private final AlertService alertService;

    public AlertController( AlertService alertService ) {
        this.alertService = alertService;
    }

    // Endpoint to retrieve and show all alerts for the user's financials.
    @GetMapping( "/" )
    public List<AlertDTO> showAllAlerts() {
        return alertService.showAllAlerts();
    }
}
