package org.rafalzajac.service;

import org.rafalzajac.domain.TeamStats;
import org.rafalzajac.repository.TeamStatsRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamStatsService {

    TeamStatsRepository teamStatsRepository;

    public TeamStatsService(TeamStatsRepository teamStatsRepository) {
        this.teamStatsRepository = teamStatsRepository;
    }

    public void saveTeamStats(TeamStats teamStats) {
        teamStatsRepository.save(teamStats);
    }
}
