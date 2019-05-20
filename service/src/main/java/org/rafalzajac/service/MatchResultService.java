package org.rafalzajac.service;

import org.rafalzajac.domain.MatchResult;
import org.rafalzajac.repository.MatchResultRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchResultService {

    private MatchResultRepository matchResultRepository;

    public MatchResultService(MatchResultRepository matchResultRepository) {
        this.matchResultRepository = matchResultRepository;
    }

    public MatchResult saveMatchResult(MatchResult matchResult){
        return matchResultRepository.save(matchResult);
    }
}
