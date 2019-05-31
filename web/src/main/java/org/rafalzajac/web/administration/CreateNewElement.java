package org.rafalzajac.web.administration;

import lombok.NoArgsConstructor;
import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.MatchResult;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.MatchResultService;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.RoundService;
import org.rafalzajac.service.TeamService;


@NoArgsConstructor
public class CreateNewElement {

    private MatchResultService matchResultService;
    private MatchService matchService;
    private RoundService roundService;
    private TeamService teamService;

    public CreateNewElement(MatchResultService matchResultService, MatchService matchService, RoundService roundService, TeamService teamService) {
        this.matchResultService = matchResultService;
        this.matchService = matchService;
        this.roundService = roundService;
        this.teamService = teamService;
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
}
