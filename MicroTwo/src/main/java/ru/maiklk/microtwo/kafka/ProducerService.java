package ru.maiklk.microtwo.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.maiklk.microtwo.dto.AbstractDto;
import ru.maiklk.microtwo.exception.KafkaMessageProcessingException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProducerService {
    KafkaTemplate<String, AbstractDto> kafkaTemplate;

    public void send(AbstractDto abstractDto) {
        if (abstractDto == null) {
            log.warn("Ошибка отправки сообщения в Kafka. Передаваемый объект null");
            return;
        }
        String topic = abstractDto.getTopicName();
        try {
            kafkaTemplate.send(topic, abstractDto);
            log.debug("Сообщение отправлено в топик {}", topic);
        } catch (Exception e) {
            log.error("Ошибка отправки сообщения {}. Сообщение не отправлено.", e.getMessage());
            throw new KafkaMessageProcessingException(e.getMessage(), e);
        }
    }
}
