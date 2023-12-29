package ru.maiklk.microone.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.maiklk.microone.dto.IndividualDto;
import ru.maiklk.microone.dto.MessageDto;
import ru.maiklk.microone.entity.Individual;
import ru.maiklk.microone.entity.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConverterDto {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public IndividualDto convertToUserVkDto(String json) {
        try {
            return objectMapper.readValue(json, IndividualDto.class);
        } catch (Exception e) {
            log.error("Не удалось конвертировать Json в объект IndividualDto {}",
                    e.getMessage());
        }
        return null;
    }

    public MessageDto convertToMessageDto(String json) {
        try {
            return objectMapper.readValue(json, MessageDto.class);
        } catch (Exception e) {
            log.error("Не удалось конвертировать Json в объект MessageDto {}",
                    e.getMessage());
        }
        return null;
    }

    public Individual fromDtoToIndividual(IndividualDto individualDto) {
        return modelMapper.map(individualDto, Individual.class);
    }

    public Message fromDtoToMessage(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }
}
