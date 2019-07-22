package org.rafalzajac.service;

import org.rafalzajac.domain.Game;
import org.rafalzajac.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void addMatch(Game game) {
        gameRepository.save(game);
    }

    public Optional<Game> getMatchById (Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> findAllMatches() {
        return gameRepository.findAll();
    }

    public void deleteMatchById(Long id) {
        gameRepository.deleteById(id);
    }
}
