package ru.maiklk.microtwo.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.maiklk.microtwo.dto.impl.MessageDto;

import java.time.Instant;
import java.util.Random;

@NoArgsConstructor
@Component
public class MessageGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MIN_TEXT_LENGTH = 3;
    private static final int MAX_TEXT_LENGTH = 50;
    private static final long MIN_CHAT_ID = 1_000_000L;
    private static final long MAX_CHAT_ID = 9_999_999L;
    private static final int MIN_MESSAGE_ID = 1;
    private static final int MAX_MESSAGE_ID = 100_000;

    private static final Random random = new Random();

    public MessageDto generateMessage() {
        return MessageDto.builder()
                .chatId(generateRandomChatId())
                .date(generateRandomDate())
                .messageId(generateRandomMessageId())
                .text(generateRandomText(random.nextInt(MIN_TEXT_LENGTH, MAX_TEXT_LENGTH + 1)))
                .build();
    }

    private long generateRandomChatId() {
        return MIN_CHAT_ID + (long) (random.nextDouble() * (MAX_CHAT_ID - MIN_CHAT_ID + 1));
    }

    private int generateRandomDate() {
        long now = Instant.now().getEpochSecond();
        long thirtyDays = 30L * 24 * 60 * 60;
        return (int) (now - random.nextInt((int) thirtyDays));
    }

    private int generateRandomMessageId() {
        return random.nextInt(MIN_MESSAGE_ID, MAX_MESSAGE_ID + 1);
    }

    public String generateRandomText(int length) {
         StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
