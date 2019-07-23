package org.rafalzajac.service;

import org.rafalzajac.domain.Round;
import org.rafalzajac.repository.RoundRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    public void addRound(Round round){
        roundRepository.save(round);
    }

    public List<Round> findAllRounds() {
        return roundRepository.findAll();
    }

    public Round findRoundByRoundNumber(int roundNumber){
        return roundRepository.findByRoundNumber(roundNumber);
    }
}
