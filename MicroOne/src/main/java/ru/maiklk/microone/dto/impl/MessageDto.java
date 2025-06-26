package ru.maiklk.microone.dto.impl;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ru.maiklk.microone.dto.AbstractDto;

@Builder
@Jacksonized
public record MessageDto(
        long chatId,
        int date,
        int messageId,
        String text
) implements AbstractDto {
}
