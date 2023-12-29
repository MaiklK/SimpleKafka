package ru.maiklk.microone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maiklk.microone.entity.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
}