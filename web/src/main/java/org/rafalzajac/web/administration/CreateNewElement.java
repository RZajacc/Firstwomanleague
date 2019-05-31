package org.rafalzajac.web.administration;

import lombok.NoArgsConstructor;
import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.MatchResult;
import org.rafalzajac.domain.Team;
import org.rafalzajac.domain.TeamStats;
import org.rafalzajac.service.*;


@NoArgsConstructor
public class CreateNewElement {

    private MatchResultService matchResultService;
    private MatchService matchService;
    private RoundService roundService;
    private TeamService teamService;
    private TeamStatsService teamStatsService;

    public CreateNewElement(MatchResultService matchResultService, MatchService matchService, RoundService roundService, TeamService teamService, TeamStatsService teamStatsService) {
        this.matchResultService = matchResultService;
        this.matchService = matchService;
        this.roundService = roundService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
    }

    public void addNewMatch(Match match) {

        //Setting match result
        MatchResult newMatchResult = new MatchResult();
        matchResultService.saveMatchResult(newMatchResult);
        match.setMatchResult(newMatchResult);

        //Setting round
        match.setRound(roundService.findRoundByRoundNumber(match.getRound().getRoundNumber()));

        //Adding current match as played by both teams
        Team hTeam = teamService.getTeamByTeamName(match.getHomeTeam());
        Team aTeam = teamService.getTeamByTeamName(match.getAwayTeam());
        hTeam.getMatchList().add(match);
        aTeam.getMatchList().add(match);

        //Saving match and updating teams
        matchService.addMatch(match);
        teamService.addTeam(hTeam);
        teamService.addTeam(aTeam);
    }

    public void addNewTeam(Team team) {
        //Adding new team stats
        TeamStats stats = new TeamStats();
        teamStatsService.saveTeamStats(stats);

        //Setting team stats for team
        team.setTeamStats(stats);

        //Saving team
        teamService.addTeam(team);
    }
}
