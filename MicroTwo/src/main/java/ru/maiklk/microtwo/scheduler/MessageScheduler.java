package ru.maiklk.microtwo.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.kafka.ProducerService;
import ru.maiklk.microtwo.util.MessageGenerator;

@Slf4j
//@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageScheduler {
    MessageGenerator messageGenerator;
    ProducerService producerService;

    @Scheduled(fixedRate = 1)
    public void scheduleMessageSending() {
        MessageDto messageDto = messageGenerator.generateMessage();
        producerService.send(messageDto);
    }
}