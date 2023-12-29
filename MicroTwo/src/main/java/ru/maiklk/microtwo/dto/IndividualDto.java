package ru.maiklk.microtwo.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IndividualDto implements AbstractDto {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
}
