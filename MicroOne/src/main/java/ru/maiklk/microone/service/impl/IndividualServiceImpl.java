package ru.maiklk.microone.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.TelegramUser;
import ru.maiklk.microone.repository.TelegramUserRepo;
import ru.maiklk.microone.service.SaveEntity;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndividualServiceImpl implements SaveEntity<TelegramUser> {
    TelegramUserRepo telegramUserRepo;

    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepo.save(telegramUser);
    }
}
