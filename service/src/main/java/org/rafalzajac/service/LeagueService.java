package org.rafalzajac.service;

import org.rafalzajac.domain.League;
import org.rafalzajac.repository.LeagueRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public void addLeague(League league) {
        leagueRepository.save(league);
    }

    public Optional<League> getLeagueById(Long id){
        return leagueRepository.findById(id);
    }


}
