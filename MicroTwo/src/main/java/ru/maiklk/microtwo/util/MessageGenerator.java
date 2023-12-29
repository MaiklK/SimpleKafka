package ru.maiklk.microtwo.util;

import org.springframework.stereotype.Component;
import ru.maiklk.microtwo.dto.MessageDto;

import java.util.Random;

@Component
public class MessageGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private MessageGenerator() {
    }

    private static final Random random = new Random();

    public MessageDto generateMessage() {
        return MessageDto.builder()
                .chatId(generateRandomChatId())
                .date(generateRandomDate())
                .messageId(generateRandomMessageId())
                .text(generateRandomText(random.nextInt(0, 10)))
                .build();
    }

    private static int generateRandomChatId() {
        return random.nextInt();
    }

    private static int generateRandomDate() {
        return random.nextInt();
    }

    private static int generateRandomMessageId() {
        return random.nextInt();
    }

    public static String generateRandomText(int length) {
        return random.ints(length, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
