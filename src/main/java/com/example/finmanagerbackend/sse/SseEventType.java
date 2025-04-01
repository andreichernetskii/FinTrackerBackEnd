package com.example.finmanagerbackend.sse;

public enum SseEventType {

    TRANSACTIONS_ALL("transactions_all"),
    LIMITS_ALL("limits_all"),
    ALERTS_ALL("alerts_all");

    private final String eventType;

    SseEventType(String eventType) {
        this.eventType = eventType;
    }
}
