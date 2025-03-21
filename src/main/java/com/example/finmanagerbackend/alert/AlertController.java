package com.example.finmanagerbackend.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controller responsible for handling alerts related to user's financials.
 */
@RequiredArgsConstructor
@RequestMapping( "/api/v1/alerts" )
@RestController
public class AlertController {

    private final AlertService alertService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Endpoint to retrieve and show all alerts for the user's financials.
    @GetMapping( "/" )
    public List<AlertDTO> showAllAlerts() {
        return alertService.showAllAlerts();
    }

    @GetMapping("/stream")
    public SseEmitter streamAlerts() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);    // Long-lived connection

        // Handle client disconnects
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        emitters.add(emitter);

        try {
            // Send initial connection confirmation
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("Connected to alerts stream")
            );
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(emitter);
        }

        return emitter;
    }

    public void sendAlertsThroughSse() {

        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(alertService.showAllAlerts());
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
    }
}
