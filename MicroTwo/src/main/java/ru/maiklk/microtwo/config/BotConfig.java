package ru.maiklk.microtwo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BotConfig {
    @Value("${bot.name}")
    private String BOT_NAME;
    @Value("${bot.token}")
    private String TOKEN;
}
