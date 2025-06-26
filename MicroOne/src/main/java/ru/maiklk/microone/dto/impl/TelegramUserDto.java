package ru.maiklk.microone.dto.impl;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ru.maiklk.microone.dto.AbstractDto;

@Builder
@Jacksonized
public record TelegramUserDto(
        long id,
        String firstName,
        String lastName,
        String userName,
        String languageCode
) implements AbstractDto {
}
