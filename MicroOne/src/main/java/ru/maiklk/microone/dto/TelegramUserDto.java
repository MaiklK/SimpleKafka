package ru.maiklk.microone.dto;

import lombok.Builder;

@Builder
public record TelegramUserDto(
        long id,
        String firstName,
        String lastName,
        String userName,
        String languageCode
) {
}
