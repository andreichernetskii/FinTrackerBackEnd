package com.example.finmanagerbackend.alert;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AlertSseAspect {

    private final AlertController alertController;

    @After("@annotation(com.example.finmanagerbackend.global.annotations.ActivateAlertsSseSending)")
    public void sendSseEvent() {
        alertController.sendAlertsThroughSse();
        System.out.println("Annotation is caught");
    }
}
