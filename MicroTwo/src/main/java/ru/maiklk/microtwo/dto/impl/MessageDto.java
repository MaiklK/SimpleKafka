package ru.maiklk.microtwo.dto.impl;

import lombok.*;
import ru.maiklk.microtwo.dto.AbstractDto;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto implements AbstractDto {
    private long chatId;
    private int date;
    private int messageId;
    private String text;
}
