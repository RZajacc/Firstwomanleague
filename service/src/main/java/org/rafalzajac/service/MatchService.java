package org.rafalzajac.service;

import org.rafalzajac.domain.Match;
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

    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    public Optional<Match> getMatchById (Long id) {
        return matchRepository.findById(id);
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }
}
