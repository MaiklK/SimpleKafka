package ru.maiklk.microone.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    private long chatId;
    @Column
    private int date;
    @Column
    private int messageId;
    @Column
    private String text;
}
