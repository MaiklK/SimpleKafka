package ru.maiklk.microtwo.dto.impl;

import lombok.*;
import ru.maiklk.microtwo.dto.AbstractDto;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TelegramUserDto implements AbstractDto {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
}
