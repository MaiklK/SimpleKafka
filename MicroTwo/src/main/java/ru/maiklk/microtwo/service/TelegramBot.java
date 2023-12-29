package ru.maiklk.microtwo.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.maiklk.microtwo.config.BotConfig;
import ru.maiklk.microtwo.dto.IndividualDto;
import ru.maiklk.microtwo.dto.MessageDto;

@Component
@Slf4j
@Builder
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final KafkaService kafkaService;

    @Autowired
    public TelegramBot(BotConfig botConfig, KafkaService kafkaService) {
        super(botConfig.getTOKEN());
        this.botConfig = botConfig;
        this.kafkaService = kafkaService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBOT_NAME();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            IndividualDto user = userBuilder(update.getMessage());
            MessageDto message = messageBuilder(update.getMessage());
            if (update.getMessage().getText().equals("/start")) {
                sendMessage(message.getChatId()
                );
                kafkaService.send(user);
            } else {
                replyMessage(message.getChatId(), message.getMessageId());
            }
            kafkaService.send(message);
            update.getMessage().setReplyToMessage(update.getMessage());
        }
    }


    private IndividualDto userBuilder(Message message) {
        return IndividualDto.builder()
                .id(message.getFrom().getId())
                .firstName(message.getFrom().getLastName())
                .lastName(message.getFrom().getLastName())
                .userName(message.getFrom().getUserName())
                .languageCode(message.getFrom().getLanguageCode())
                .build();
    }

    private MessageDto messageBuilder(Message message) {
        return MessageDto.builder()
                .chatId(message.getChatId())
                .date(message.getDate())
                .messageId(message.getMessageId())
                .text(message.getText())
                .build();
    }

    private void replyMessage(long chatId, int messageId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "\uD83C\uDF85");
        sendMessage.setReplyToMessageId(messageId);

        executeSendMessage(sendMessage);
    }

    private void sendMessage(long chatId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),
                "Заработало!!!\uD83C\uDF8A\uD83C\uDF8A\uD83C\uDF8A\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89");

        executeSendMessage(sendMessage);
    }

    private void executeSendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка API телеграмм: {}", e.getMessage());
        }
    }
}
