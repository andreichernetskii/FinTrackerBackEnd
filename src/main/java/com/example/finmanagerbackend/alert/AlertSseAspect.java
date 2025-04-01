package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.sse.SseEvent;
import com.example.finmanagerbackend.sse.SseEventType;
import com.example.finmanagerbackend.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Aspect
@Component
public class AlertSseAspect {

    private final SseService sseService;
    private final AlertService alertService;

    @After("@annotation(com.example.finmanagerbackend.global.annotations.SendAlerts)")
    public void sendSseEvent() {
        sseService.sendData(
                SseEvent.<List<AlertDTO>>builder()
                        .eventType(SseEventType.ALERTS_ALL)
                        .data(alertService.showAllAlerts())
                        .build()
        );
    }
}
