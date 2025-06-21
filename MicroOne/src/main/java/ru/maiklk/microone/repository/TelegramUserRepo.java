package ru.maiklk.microone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maiklk.microone.entity.TelegramUser;

public interface TelegramUserRepo extends JpaRepository<TelegramUser, Long> {
}
