package ru.maiklk.microtwo.dto.impl;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ru.maiklk.microtwo.dto.AbstractDto;

@Builder
@Jacksonized
public record MessageDto(
        long chatId,
        int date,
        int messageId,
        String text
) implements AbstractDto {
    @Override
    public String getTopicName() {
        return "telegram_message";
    }
}

