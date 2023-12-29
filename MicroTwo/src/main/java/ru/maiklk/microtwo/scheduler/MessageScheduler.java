package ru.maiklk.microtwo.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.maiklk.microtwo.dto.MessageDto;
import ru.maiklk.microtwo.service.KafkaService;
import ru.maiklk.microtwo.util.MessageGenerator;

@Slf4j
//@Component
@RequiredArgsConstructor
public class MessageScheduler {
    private final MessageGenerator messageGenerator;
    private final KafkaService kafkaService;

    @Scheduled(fixedRate = 1)
    public void scheduleMessageSending() {
        MessageDto messageDto = messageGenerator.generateMessage();
        kafkaService.send(messageDto);
    }
}