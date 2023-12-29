package ru.maiklk.microtwo.dto;

import lombok.*;

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
