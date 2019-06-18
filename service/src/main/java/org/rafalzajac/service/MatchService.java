package org.rafalzajac.service;

import org.rafalzajac.domain.Game;
import org.rafalzajac.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Game addMatch(Game game) {
        return matchRepository.save(game);
    }

    public Optional<Game> getMatchById (Long id) {
        return matchRepository.findById(id);
    }

    public List<Game> findAllMatches() {
        return matchRepository.findAll();
    }
}
