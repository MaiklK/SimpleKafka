package ru.maiklk.microone.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.repository.MessageRepo;
import ru.maiklk.microone.service.SaveEntity;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl implements SaveEntity<Message> {
    MessageRepo messageRepo;

    @Override
    public void save(Message message) {
        messageRepo.save(message);
    }
}
