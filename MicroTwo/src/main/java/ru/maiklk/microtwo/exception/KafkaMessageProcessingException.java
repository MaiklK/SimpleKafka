package ru.maiklk.microtwo.exception;

public class KafkaMessageProcessingException extends RuntimeException {
    public KafkaMessageProcessingException(String message, Exception cause) {
        super(message, cause);
    }
}
