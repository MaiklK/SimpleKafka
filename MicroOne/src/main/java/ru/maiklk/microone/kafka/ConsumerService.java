package ru.maiklk.microone.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.dto.MessageDto;
import ru.maiklk.microone.dto.TelegramUserDto;
import ru.maiklk.microone.service.impl.IndividualServiceImpl;
import ru.maiklk.microone.service.impl.MessageServiceImpl;
import ru.maiklk.microone.util.ConverterDto;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumerService {

    private final String TOPIC_USER = "${topic_user}";
    private static final String TOPIC_MESSAGE = "${topic_message}";
    private static final String GROUP_ID = "${group_id}";
    ConverterDto converterDto;
    IndividualServiceImpl individualService;
    MessageServiceImpl messageService;

    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = TOPIC_USER, groupId = GROUP_ID)
    public void consumerUser(String message) {
        try {
            TelegramUserDto dto = converterDto.convertToUserVkDto(message);
            individualService.save(converterDto.fromDtoToIndividual(dto));
            log.info(dto.toString());
        } catch (Exception e) {
            dlt(message, TOPIC_USER);
            error(e, TOPIC_USER, message);
        }
    }

    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = TOPIC_MESSAGE, groupId = GROUP_ID)
    public void consumerMessage(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            MessageDto dto = converterDto.convertToMessageDto(message);
            messageService.save(converterDto.fromDtoToMessage(dto));
            log.info("Сообщение успешно обработано: {} из топика {}", dto, topic);
        } catch (Exception e) {
            dlt(message, TOPIC_MESSAGE);
            error(e, TOPIC_MESSAGE, message);
        }
    }

    @DltHandler
    public void dlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("{} from {}", in, topic);
    }

    private Exception error(Exception e, String topic, String message) {
        log.error("Ошибка при обработке сообщения из топика {}: {}. Сообщение: {}", topic, e.getMessage(), message, e);
        throw new RuntimeException(e);
    }
}
