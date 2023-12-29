package ru.maiklk.microone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto {
    @JsonProperty
    private long chatId;
    @JsonProperty
    private int date;
    @JsonProperty
    private int messageId;
    @JsonProperty
    private String text;
}
