package ru.maiklk.microtwo.dto.impl;

import lombok.Builder;
import ru.maiklk.microtwo.dto.AbstractDto;

@Builder
public record MessageDto (
    long chatId,
    int date,
    int messageId,
    String text
    )implements AbstractDto {
}

