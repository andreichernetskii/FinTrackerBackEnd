package com.example.finmanagerbackend.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@Service
public class SseService {

//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {

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
                    .data("Connected to SSE")
            );

        } catch (IOException e) {
            emitter.complete();
            emitters.remove(emitter);
        }

        return emitter;
    }

    public void sendData(SseEvent<?> event) {

        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(event));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
    }
}
