package com.example.finmanagerbackend.alert;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping( "/api/v1/alerts" )
public class AlertController {
    private final AlertService alertService;

    public AlertController( AlertService alertService ) {
        this.alertService = alertService;
    }

    @GetMapping()
    public List<AlertDTO> showAllAlerts() {
        return alertService.showAllAlerts();
    }
}
