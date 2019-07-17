package org.rafalzajac.service;

import org.rafalzajac.domain.GameResult;
import org.rafalzajac.repository.MatchResultRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchResultService {

    private MatchResultRepository matchResultRepository;

    public MatchResultService(MatchResultRepository matchResultRepository) {
        this.matchResultRepository = matchResultRepository;
    }

    public void saveMatchResult(GameResult gameResult){
        matchResultRepository.save(gameResult);
    }
}
