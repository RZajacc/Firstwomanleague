package org.rafalzajac.service;

import org.rafalzajac.domain.Round;
import org.rafalzajac.repository.RoundRepository;
import org.springframework.stereotype.Service;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    public Round addRound(Round round){
        return roundRepository.save(round);
    }
}
