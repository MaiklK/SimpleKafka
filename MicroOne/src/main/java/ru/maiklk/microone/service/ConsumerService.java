package ru.maiklk.microone.service;

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
import ru.maiklk.microone.dto.IndividualDto;
import ru.maiklk.microone.dto.MessageDto;
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

    @KafkaListener(topics = TOPIC_USER, groupId = GROUP_ID)
    public void consumerUser(String message) {
        IndividualDto dto = converterDto.convertToUserVkDto(message);
        individualService.save(converterDto.fromDtoToIndividual(dto));
        log.info(dto.toString());
    }

    @KafkaListener(topics = TOPIC_MESSAGE, groupId = GROUP_ID)
    public void consumerMessage(String message) {
        MessageDto dto = converterDto.convertToMessageDto(message);
        messageService.save(converterDto.fromDtoToMessage(dto));
        log.info(dto.toString());
    }

    //паттерн ретрай и DLQ
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 5.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = TOPIC_MESSAGE, groupId = GROUP_ID)
    public void listen(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.warn("{} from {}", in, topic);
        throw new RuntimeException("ERROR!!!!");
    }

    @DltHandler
    public void dlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("{} from {}", in, topic);
    }
}
