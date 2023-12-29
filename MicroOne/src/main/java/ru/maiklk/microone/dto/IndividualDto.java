package ru.maiklk.microone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IndividualDto {
    @JsonProperty
    private long id;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String userName;
    @JsonProperty
    private String languageCode;
}
