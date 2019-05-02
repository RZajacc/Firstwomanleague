package org.rafalzajac.service;

import org.rafalzajac.domain.Team;
import org.rafalzajac.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team addTeam (Team team) {
        return teamRepository.save(team);
    }
}
