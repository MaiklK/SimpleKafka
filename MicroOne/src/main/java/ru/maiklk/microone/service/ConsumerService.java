package ru.maiklk.microone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.maiklk.microone.util.ConverterDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private static final String TOPIC_USER = "${topic_user}";
    private static final String TOPIC_MESSAGE = "${topic_message}";
    private static final String GROUP_ID = "${group_id}";
    private final ConverterDto converterDto;
    private final IndividualServiceImpl individualService;
    private final MessageServiceImpl messageService;

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
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 5.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = TOPIC_MESSAGE, groupId = GROUP_ID)
    public void listen(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.warn(in + " from " + topic);
        throw new RuntimeException("ERROR!!!!");
    }

    @DltHandler
    public void dlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error(in + " from " + topic);
    }
}
