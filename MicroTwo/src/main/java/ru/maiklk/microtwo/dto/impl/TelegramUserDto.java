package ru.maiklk.microtwo.dto.impl;

import lombok.Builder;
import ru.maiklk.microtwo.dto.AbstractDto;

@Builder
public record TelegramUserDto(
        long id,
        String firstName,
        String lastName,
        String userName,
        String languageCode
) implements AbstractDto {
    @Override
    public String getTopicName() {
        return "telegram_user";
    }
}
