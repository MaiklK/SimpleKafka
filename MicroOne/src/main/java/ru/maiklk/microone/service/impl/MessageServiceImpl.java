package ru.maiklk.microone.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.repository.MessageRepo;
import ru.maiklk.microone.service.SaveEntity;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements SaveEntity<Message> {
    MessageRepo messageRepo;

    @Override
    public void saveMessage(Message message) {
        if (message == null) {
            log.error("Сообщение не должно быть null");
            return;
        }
        try {
            messageRepo.save(message);
            log.debug("Сообщение: {}.\nУспешно сохранено в БД", message);
        } catch (Exception e) {
            log.error("Ошибка: {}.\nПри сохранении сообщения: {}", e.getMessage(), message);
        }
    }
}
