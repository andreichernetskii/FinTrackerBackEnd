package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.sse.SseEvent;
import com.example.finmanagerbackend.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Aspect
@Component
public class LimitSseAspect {

    private final SseService sseService;
    private final LimitService limitService;

    @After("@annotation(com.example.finmanagerbackend.global.annotations.SendLimits)")
    public void sendAllLimitsOfAccount() {
        sseService.sendData(
                SseEvent.<List<Limit>>builder()
                        .eventType("limits")
                        .data(limitService.getLimits())
                        .build()
        );
    }
}
