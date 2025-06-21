package ru.maiklk.microone.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.maiklk.microone.dto.MessageDto;
import ru.maiklk.microone.dto.TelegramUserDto;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.entity.TelegramUser;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConverterDto {
    ObjectMapper objectMapper;
    ModelMapper modelMapper;

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Не удалось конвертировать Json в объект {}: {}", clazz.getSimpleName(), e.getMessage());
            throw new RuntimeException("Ошибка конвертации JSON в " + clazz.getSimpleName(), e);
        }
    }

    public TelegramUserDto convertToUserVkDto(String json) {
        return fromJson(json, TelegramUserDto.class);
    }

    public MessageDto convertToMessageDto(String json) {
        return fromJson(json, MessageDto.class);
    }

    public TelegramUser fromDtoToIndividual(TelegramUserDto telegramUserDto) {
        return modelMapper.map(telegramUserDto, TelegramUser.class);
    }

    public Message fromDtoToMessage(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }
}
