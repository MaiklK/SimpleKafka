package ru.maiklk.microone.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maiklk.microone.dto.AbstractDto;
import ru.maiklk.microone.dto.impl.MessageDto;
import ru.maiklk.microone.dto.impl.TelegramUserDto;
import ru.maiklk.microone.exception.KafkaMessageProcessingException;
import ru.maiklk.microone.service.impl.MessageServiceImpl;
import ru.maiklk.microone.service.impl.TelegramUserServiceImpl;
import ru.maiklk.microone.util.ConverterDto;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerService {

    private static final String TOPIC_USER = "${kafka.topic_user}";
    private static final String TOPIC_MESSAGE = "${kafka.topic_message}";
    private static final String GROUP_ID = "${kafka.group_id}";
    ConverterDto converterDto;
    TelegramUserServiceImpl telegramUserService;
    MessageServiceImpl messageService;

    @Transactional
    @RetryableTopic
    @KafkaListener(topics = TOPIC_USER, groupId = GROUP_ID)
    public void sendTelegramUser(String message) {
        try {
            TelegramUserDto dto = converterDto.parseTelegramUserDto(message);
            telegramUserService.saveEntity(converterDto.mapToTelegramUser(dto));
            logSuccess(dto, TOPIC_USER);
        } catch (Exception e) {
            handleProcessingError(e, TOPIC_USER, message);
        }
    }

    @Transactional
    @RetryableTopic
    @KafkaListener(topics = TOPIC_MESSAGE, groupId = GROUP_ID)
    public void sendMessage(String message) {
        try {
            MessageDto dto = converterDto.parseMessageDto(message);
            messageService.saveEntity(converterDto.mapToMessage(dto));
            logSuccess(dto, TOPIC_MESSAGE);
        } catch (Exception e) {
            handleProcessingError(e, TOPIC_MESSAGE, message);
        }
    }

    @DltHandler
    public void dlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("{} from {}", in, topic);
    }

    private void logSuccess(AbstractDto dto, String topic) {
        log.info("Сообщение успешно обработано: {} из топика {}", dto, topic);
    }

    private void handleProcessingError(Exception e, String topic, String message) {
        log.error("Ошибка при обработке сообщения из топика {}: {}. Сообщение: {}", topic, e.getMessage(), message, e);
        dlt(message, topic);
        throw new KafkaMessageProcessingException(e.getMessage(), e);
    }
}
