package org.rafalzajac.web.fileProcessing;

import org.rafalzajac.domain.Match;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.TeamService;

import java.util.Optional;


public class ProcessMatchResult {

    private TeamService teamService;
    private MatchService matchService;

    public ProcessMatchResult(TeamService teamService, MatchService matchService) {
        this.teamService = teamService;
        this.matchService = matchService;
    }

    public void addMatchResult (Match match) {

        Optional<Match> selected = matchService.getMatchById(match.getId());
        if(selected.isPresent()) {
            Match matchToUpdate = selected.get();

            //Sets won by both teams
            matchToUpdate.getMatchResult().setHomeTeamSetsWon(match.getMatchResult().getHomeTeamSetsWon());
            matchToUpdate.getMatchResult().setAwayTeamSetsWon(match.getMatchResult().getAwayTeamSetsWon());

            //Home team each set score
            matchToUpdate.getMatchResult().setHomeTeamSet1Score(match.getMatchResult().getHomeTeamSet1Score());
            matchToUpdate.getMatchResult().setHomeTeamSet2Score(match.getMatchResult().getHomeTeamSet2Score());
            matchToUpdate.getMatchResult().setHomeTeamSet3Score(match.getMatchResult().getHomeTeamSet3Score());
            matchToUpdate.getMatchResult().setHomeTeamSet4Score(match.getMatchResult().getHomeTeamSet4Score());
            matchToUpdate.getMatchResult().setHomeTeamSet5Score(match.getMatchResult().getHomeTeamSet5Score());

            //Away team each set score
            matchToUpdate.getMatchResult().setAwayTeamSet1Score(match.getMatchResult().getAwayTeamSet1Score());
            matchToUpdate.getMatchResult().setAwayTeamSet2Score(match.getMatchResult().getAwayTeamSet2Score());
            matchToUpdate.getMatchResult().setAwayTeamSet3Score(match.getMatchResult().getAwayTeamSet3Score());
            matchToUpdate.getMatchResult().setAwayTeamSet4Score(match.getMatchResult().getAwayTeamSet4Score());
            matchToUpdate.getMatchResult().setAwayTeamSet5Score(match.getMatchResult().getAwayTeamSet5Score());

            matchService.addMatch(matchToUpdate);
        }
    }
}
