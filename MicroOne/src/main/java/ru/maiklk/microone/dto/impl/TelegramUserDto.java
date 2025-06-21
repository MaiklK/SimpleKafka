package ru.maiklk.microone.dto.impl;

import lombok.Builder;
import ru.maiklk.microone.dto.AbstractDto;

@Builder
public record TelegramUserDto(
        long id,
        String firstName,
        String lastName,
        String userName,
        String languageCode
) implements AbstractDto {
}
