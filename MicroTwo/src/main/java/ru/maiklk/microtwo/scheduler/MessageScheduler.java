package ru.maiklk.microtwo.scheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.service.KafkaService;
import ru.maiklk.microtwo.util.MessageGenerator;

@Slf4j
//@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageScheduler {
    MessageGenerator messageGenerator;
    KafkaService kafkaService;

    @Scheduled(fixedRate = 1)
    public void scheduleMessageSending() {
        MessageDto messageDto = messageGenerator.generateMessage();
        kafkaService.send(messageDto);
    }
}