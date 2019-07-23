package org.rafalzajac.service;

import org.rafalzajac.domain.Team;
import org.rafalzajac.repository.TeamRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void addTeam (Team team) {
        teamRepository.save(team);
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Team getTeamByTag(String tag) {
        return teamRepository.findByTeamTag(tag);
    }

    public Team getTeamByTeamName(String teamName){
        return teamRepository.findByTeamName(teamName);
    }
}
