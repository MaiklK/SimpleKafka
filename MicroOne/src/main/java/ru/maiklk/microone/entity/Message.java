package ru.maiklk.microone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table
public class Message {
    @Id
    private long chatId;
    @Column
    private int date;
    @Column
    private int messageId;
    @Column
    private String text;
}
