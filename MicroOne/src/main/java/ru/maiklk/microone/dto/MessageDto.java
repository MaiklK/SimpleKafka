package ru.maiklk.microone.dto;

import lombok.Builder;

@Builder
public record MessageDto(
        long chatId,
        int date,
        int messageId,
        String text) {
}
