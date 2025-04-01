package com.example.finmanagerbackend.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsible for handling alerts related to user's financials.
 */
@RequiredArgsConstructor
@RequestMapping( "/api/v1/alerts" )
@RestController
public class AlertController {

    private final AlertService alertService;

    // Endpoint to retrieve and show all alerts for the user's financials.
    @GetMapping( "/" )
    public List<AlertDTO> showAllAlerts() {
        return alertService.showAllAlerts();
    }
}
