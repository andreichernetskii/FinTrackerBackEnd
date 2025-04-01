package com.example.finmanagerbackend.sse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SseEvent<T> {

    private SseEventType eventType;
    private T data;
}
