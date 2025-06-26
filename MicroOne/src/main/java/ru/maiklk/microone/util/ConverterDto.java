package ru.maiklk.microone.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.maiklk.microone.dto.impl.MessageDto;
import ru.maiklk.microone.dto.impl.TelegramUserDto;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.entity.TelegramUser;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConverterDto {
    ObjectMapper objectMapper;
    ModelMapper modelMapper;

    public TelegramUserDto parseTelegramUserDto(String json) {
        return fromJson(json, TelegramUserDto.class);
    }

    public MessageDto parseMessageDto(String json) {
        return fromJson(json, MessageDto.class);
    }

    public TelegramUser mapToTelegramUser(TelegramUserDto telegramUserDto) {
        return modelMapper.map(telegramUserDto, TelegramUser.class);
    }

    public Message mapToMessage(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Не удалось конвертировать Json в объект {}: {}", clazz.getSimpleName(), e.getMessage());
            throw new RuntimeException("Ошибка конвертации JSON в " + clazz.getSimpleName(), e);
        }
    }
}
