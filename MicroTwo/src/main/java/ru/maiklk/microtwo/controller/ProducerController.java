package ru.maiklk.microtwo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maiklk.microtwo.dto.impl.TelegramUserDto;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.kafka.ProducerService;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping("/sendTelegramUser")
    public ResponseEntity<String> sendMessage(@RequestBody TelegramUserDto dto) {
        producerService.send(dto);
        return new ResponseEntity<>("TelegramUser send", HttpStatus.OK);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto dto) {
        producerService.send(dto);
        return new ResponseEntity<>("Message send", HttpStatus.OK);
    }
}
