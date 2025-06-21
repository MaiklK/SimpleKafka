package ru.maiklk.microone.dto.impl;

import lombok.Builder;
import ru.maiklk.microone.dto.AbstractDto;

@Builder
public record MessageDto(
        long chatId,
        int date,
        int messageId,
        String text
) implements AbstractDto {
}
