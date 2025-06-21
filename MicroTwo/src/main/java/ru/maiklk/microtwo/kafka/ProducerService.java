package ru.maiklk.microtwo.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.maiklk.microtwo.dto.AbstractDto;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.dto.impl.TelegramUserDto;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, AbstractDto> kafkaTemplate;

    @Value("${topic_user}")
    private String TOPIC_USER;
    @Value("${topic_message}")
    private String TOPIC_MESSAGE;

    public void send(AbstractDto abstractDto) {
        if (abstractDto instanceof TelegramUserDto) {
            kafkaTemplate.send(TOPIC_USER, abstractDto);
        }
        if (abstractDto instanceof MessageDto) {
            kafkaTemplate.send(TOPIC_MESSAGE, abstractDto);
        }
    }
}
