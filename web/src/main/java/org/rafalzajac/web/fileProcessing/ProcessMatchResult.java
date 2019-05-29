package org.rafalzajac.web.fileProcessing;

import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.Team;
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
            updateTeamStats(matchToUpdate);
            matchService.addMatch(matchToUpdate);
        }


    }


    public void updateTeamStats(Match match) {

        Team homeTeam = match.getTeams().get(0);
        Team awayTeam = match.getTeams().get(1);

        if(match.getMatchResult().getAwayTeamSetsWon() != 0 || match.getMatchResult().getHomeTeamSetsWon() != 0){

            //Setting matches played for both teams
            homeTeam.getTeamStats().setMatchPlayed(homeTeam.getTeamStats().getMatchPlayed() + 1);
            awayTeam.getTeamStats().setMatchPlayed(awayTeam.getTeamStats().getMatchPlayed() + 1);

            //Setting sets won and lost for both teams
            homeTeam.getTeamStats().setSetsWon(homeTeam.getTeamStats().getSetsWon() + match.getMatchResult().getHomeTeamSetsWon());
            homeTeam.getTeamStats().setSetsLost(homeTeam.getTeamStats().getSetsLost() + match.getMatchResult().getAwayTeamSetsWon());
            awayTeam.getTeamStats().setSetsWon(awayTeam.getTeamStats().getSetsWon() + match.getMatchResult().getAwayTeamSetsWon());
            awayTeam.getTeamStats().setSetsLost(awayTeam.getTeamStats().getSetsLost() + match.getMatchResult().getHomeTeamSetsWon());

            //Setting sets ratio for home team
            if (homeTeam.getTeamStats().getSetsLost() == 0) {
                homeTeam.getTeamStats().setSetRatio(homeTeam.getTeamStats().getSetsWon());
            }else {
                homeTeam.getTeamStats().setSetRatio( (float)homeTeam.getTeamStats().getSetsWon() / homeTeam.getTeamStats().getSetsLost());
            }

            //Setting sets ratio for away team
            if (awayTeam.getTeamStats().getSetsLost() == 0) {
                awayTeam.getTeamStats().setSetRatio(awayTeam.getTeamStats().getSetsWon());
            }else {
                awayTeam.getTeamStats().setSetRatio( (float)awayTeam.getTeamStats().getSetsWon() / awayTeam.getTeamStats().getSetsLost());
            }

            //Setting points scored by both teams
                homeTeam.getTeamStats().setPointsWon(homeTeam.getTeamStats().getPointsWon() + match.getMatchResult().getHomeTeamSet1Score() + match.getMatchResult().getHomeTeamSet2Score() + match.getMatchResult().getHomeTeamSet3Score() + match.getMatchResult().getHomeTeamSet4Score() + match.getMatchResult().getHomeTeamSet5Score());
                homeTeam.getTeamStats().setPointsLost(homeTeam.getTeamStats().getPointsLost() + match.getMatchResult().getAwayTeamSet1Score() + match.getMatchResult().getAwayTeamSet2Score() + match.getMatchResult().getAwayTeamSet3Score() + match.getMatchResult().getAwayTeamSet4Score() + match.getMatchResult().getAwayTeamSet5Score());
                homeTeam.getTeamStats().setTeamPointsRatio((float) homeTeam.getTeamStats().getPointsWon() / homeTeam.getTeamStats().getPointsLost());
                awayTeam.getTeamStats().setPointsWon(awayTeam.getTeamStats().getPointsWon() + match.getMatchResult().getAwayTeamSet1Score() + match.getMatchResult().getAwayTeamSet2Score() + match.getMatchResult().getAwayTeamSet3Score() + match.getMatchResult().getAwayTeamSet4Score() + match.getMatchResult().getAwayTeamSet5Score());
                awayTeam.getTeamStats().setPointsLost(awayTeam.getTeamStats().getPointsLost() + match.getMatchResult().getHomeTeamSet1Score() + match.getMatchResult().getHomeTeamSet2Score() + match.getMatchResult().getHomeTeamSet3Score() + match.getMatchResult().getHomeTeamSet4Score() + match.getMatchResult().getHomeTeamSet5Score());
                awayTeam.getTeamStats().setTeamPointsRatio((float) awayTeam.getTeamStats().getPointsWon() / awayTeam.getTeamStats().getPointsLost());

            //Setting league points, matches won and matches lost
            if( match.getMatchResult().getHomeTeamSetsWon() == 3 && (match.getMatchResult().getAwayTeamSetsWon() == 0 || match.getMatchResult().getAwayTeamSetsWon() == 1) ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 3);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);

            } else if ( match.getMatchResult().getHomeTeamSetsWon() == 3 && match.getMatchResult().getAwayTeamSetsWon() == 2 ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 2);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);
                awayTeam.getTeamStats().setLeaguePoints(awayTeam.getTeamStats().getLeaguePoints() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);
            } else if ( (match.getMatchResult().getHomeTeamSetsWon() == 0 || match.getMatchResult().getHomeTeamSetsWon() == 1) && match.getMatchResult().getAwayTeamSetsWon() == 3 ) {
                awayTeam.getTeamStats().setLeaguePoints(awayTeam.getTeamStats().getLeaguePoints() + 3);
                awayTeam.getTeamStats().setMatchWon(awayTeam.getTeamStats().getMatchWon() + 1);

                homeTeam.getTeamStats().setMatchLost(homeTeam.getTeamStats().getMatchLost() + 1);
            } else {
                awayTeam.getTeamStats().setLeaguePoints(awayTeam.getTeamStats().getLeaguePoints() + 2);
                awayTeam.getTeamStats().setMatchWon(awayTeam.getTeamStats().getMatchWon() + 1);
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 1);

                homeTeam.getTeamStats().setMatchLost(homeTeam.getTeamStats().getMatchLost() + 1);
            }

            teamService.addTeam(homeTeam);
            teamService.addTeam(awayTeam);
        }
    }
}
