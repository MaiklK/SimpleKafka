package ru.maiklk.microtwo.telegram;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.maiklk.microtwo.config.BotConfig;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.dto.impl.TelegramUserDto;
import ru.maiklk.microtwo.kafka.ProducerService;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramBot extends TelegramLongPollingBot {
    BotConfig botConfig;
    ProducerService producerService;

    @Autowired
    public TelegramBot(BotConfig botConfig, ProducerService producerService) {
        super(botConfig.getTOKEN());
        this.botConfig = botConfig;
        this.producerService = producerService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBOT_NAME();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            TelegramUserDto telegramUserDto = userBuilder(message);
            MessageDto messageDto = messageBuilder(message);

            if ("/start".equals(message.getText())) {
                producerService.send(telegramUserDto);
                sendStartMessage(message.getChatId());
            } else {
                producerService.send(messageDto);
                replyMessage(message.getChatId(), message.getMessageId());
            }

        }
    }


    private TelegramUserDto userBuilder(Message message) {
        return TelegramUserDto.builder()
                .id(message.getFrom().getId())
                .firstName(message.getFrom().getFirstName())
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
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "отправил твое сообщение в кафку");
        sendMessage.setReplyToMessageId(messageId);
        executeSendMessage(sendMessage);
    }

    private void sendStartMessage(long chatId) {
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
