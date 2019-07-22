package org.rafalzajac.service;

import org.rafalzajac.domain.GameResult;
import org.rafalzajac.repository.GameResultRepository;
import org.springframework.stereotype.Service;

@Service
public class GameResultService {

    private GameResultRepository gameResultRepository;

    public GameResultService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    public void saveMatchResult(GameResult gameResult){
        gameResultRepository.save(gameResult);
    }
}
