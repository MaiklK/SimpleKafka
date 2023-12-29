package ru.maiklk.microone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maiklk.microone.entity.Individual;

public interface IndividualRepo extends JpaRepository<Individual, Long> {
}
