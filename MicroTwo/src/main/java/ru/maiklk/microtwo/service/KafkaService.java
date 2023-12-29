package ru.maiklk.microtwo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.maiklk.microtwo.dto.AbstractDto;
import ru.maiklk.microtwo.dto.IndividualDto;
import ru.maiklk.microtwo.dto.MessageDto;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, AbstractDto> kafkaIndividual;

    @Value("${topic_user}")
    private String TOPIC_USER;
    @Value("${topic_message}")
    private String TOPIC_MESSAGE;

    public void send(AbstractDto abstractDto) {
        if (abstractDto instanceof IndividualDto) {
            kafkaIndividual.send(TOPIC_USER, abstractDto);
        }
        if (abstractDto instanceof MessageDto) {
            kafkaIndividual.send(TOPIC_MESSAGE, abstractDto);
        }

    }

}
