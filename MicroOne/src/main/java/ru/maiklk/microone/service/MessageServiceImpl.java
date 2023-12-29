package ru.maiklk.microone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.repository.MessageRepo;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements SaveEntity<Message> {
    private final MessageRepo messageRepo;

    @Override
    public void save(Message message) {
        messageRepo.save(message);
    }
}
