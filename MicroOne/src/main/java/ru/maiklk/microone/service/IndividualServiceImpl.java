package ru.maiklk.microone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maiklk.microone.entity.Individual;
import ru.maiklk.microone.repository.IndividualRepo;

@Service
@RequiredArgsConstructor
public class IndividualServiceImpl implements SaveEntity<Individual> {
    private final IndividualRepo individualRepo;

    @Override
    public void save(Individual individual) {
        individualRepo.save(individual);
    }
}
