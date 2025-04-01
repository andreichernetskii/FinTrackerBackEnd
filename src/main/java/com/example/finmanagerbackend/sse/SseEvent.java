package com.example.finmanagerbackend.sse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SseEvent<T> {

    private String eventType;
    private T data;
}
