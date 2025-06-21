package ru.maiklk.microone.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.TelegramUser;
import ru.maiklk.microone.repository.TelegramUserRepo;
import ru.maiklk.microone.service.SaveEntity;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramUserServiceImpl implements SaveEntity<TelegramUser> {
    TelegramUserRepo telegramUserRepo;

    @Override
    public void saveMessage(TelegramUser telegramUser) {
        if (telegramUser == null) {
            log.error("Пользователь не должен быть null");
            return;
        }
        try {
            telegramUserRepo.save(telegramUser);
            log.debug("Пользователь: {}.\nУспешно сохранён в БД", telegramUser);
        } catch (Exception e) {
            log.error("Ошибка: {}.\nПри сохранении пользователя: {}", e.getMessage(), telegramUser);
        }
    }
}
