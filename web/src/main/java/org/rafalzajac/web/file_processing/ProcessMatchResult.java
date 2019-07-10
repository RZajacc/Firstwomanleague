package org.rafalzajac.web.file_processing;

import org.rafalzajac.domain.Game;
import org.rafalzajac.domain.MatchResult;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.MatchResultService;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.TeamService;
import org.rafalzajac.service.TeamStatsService;
import java.util.Optional;


public class ProcessMatchResult {

    private TeamService teamService;
    private MatchService matchService;
    private TeamStatsService teamStatsService;
    private MatchResultService matchResultService;


    public ProcessMatchResult(TeamService teamService, MatchService matchService, TeamStatsService teamStatsService, MatchResultService matchResultService) {
        this.teamService = teamService;
        this.matchService = matchService;
        this.teamStatsService = teamStatsService;
        this.matchResultService = matchResultService;
    }

    public void addMatchResult (Game game) {

        Optional<Game> selected = matchService.getMatchById(game.getId());

        if(selected.isPresent()) {

            Game matchToUpdate = selected.get();
            MatchResult matchResult = game.getMatchResult();

            if (matchToUpdate.getMatchResult().getHomeTeamSetsWon() == 0 && matchToUpdate.getMatchResult().getAwayTeamSetsWon() == 0) {
                matchResultService.saveMatchResult(matchResult);
                matchToUpdate.setMatchResult(matchResult);
                matchService.addMatch(matchToUpdate);
                addTeamStats(matchToUpdate);
            }
        }
    }


    public void addTeamStats(Game game) {

        Team homeTeam = game.getTeams().get(0);
        Team awayTeam = game.getTeams().get(1);

//        TeamStats homeTeam = hTeam.getTeamStats();
//        TeamStats awayTeam = aTeam.getTeamStats();


        if(game.getMatchResult().getAwayTeamSetsWon() != 0 || game.getMatchResult().getHomeTeamSetsWon() != 0){

            //Setting matches played for both teams
            homeTeam.getTeamStats().setMatchPlayed(homeTeam.getTeamStats().getMatchPlayed() + 1);
            awayTeam.getTeamStats().setMatchPlayed(awayTeam.getTeamStats().getMatchPlayed() + 1);

            //Setting sets won and lost for both teams
            homeTeam.getTeamStats().setSetsWon(homeTeam.getTeamStats().getSetsWon() + game.getMatchResult().getHomeTeamSetsWon());
            homeTeam.getTeamStats().setSetsLost(homeTeam.getTeamStats().getSetsLost() + game.getMatchResult().getAwayTeamSetsWon());
            awayTeam.getTeamStats().setSetsWon(awayTeam.getTeamStats().getSetsWon() + game.getMatchResult().getAwayTeamSetsWon());
            awayTeam.getTeamStats().setSetsLost(awayTeam.getTeamStats().getSetsLost() + game.getMatchResult().getHomeTeamSetsWon());

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
                homeTeam.getTeamStats().setPointsWon(homeTeam.getTeamStats().getPointsWon() + game.getMatchResult().getHomeTeamSet1Score() + game.getMatchResult().getHomeTeamSet2Score() + game.getMatchResult().getHomeTeamSet3Score() + game.getMatchResult().getHomeTeamSet4Score() + game.getMatchResult().getHomeTeamSet5Score());
                homeTeam.getTeamStats().setPointsLost(homeTeam.getTeamStats().getPointsLost() + game.getMatchResult().getAwayTeamSet1Score() + game.getMatchResult().getAwayTeamSet2Score() + game.getMatchResult().getAwayTeamSet3Score() + game.getMatchResult().getAwayTeamSet4Score() + game.getMatchResult().getAwayTeamSet5Score());
                homeTeam.getTeamStats().setTeamPointsRatio((float) homeTeam.getTeamStats().getPointsWon() / homeTeam.getTeamStats().getPointsLost());
                awayTeam.getTeamStats().setPointsWon(awayTeam.getTeamStats().getPointsWon() + game.getMatchResult().getAwayTeamSet1Score() + game.getMatchResult().getAwayTeamSet2Score() + game.getMatchResult().getAwayTeamSet3Score() + game.getMatchResult().getAwayTeamSet4Score() + game.getMatchResult().getAwayTeamSet5Score());
                awayTeam.getTeamStats().setPointsLost(awayTeam.getTeamStats().getPointsLost() + game.getMatchResult().getHomeTeamSet1Score() + game.getMatchResult().getHomeTeamSet2Score() + game.getMatchResult().getHomeTeamSet3Score() + game.getMatchResult().getHomeTeamSet4Score() + game.getMatchResult().getHomeTeamSet5Score());
                awayTeam.getTeamStats().setTeamPointsRatio((float) awayTeam.getTeamStats().getPointsWon() / awayTeam.getTeamStats().getPointsLost());

            //Setting league points, matches won and matches lost
            if( game.getMatchResult().getHomeTeamSetsWon() == 3 && (game.getMatchResult().getAwayTeamSetsWon() == 0 || game.getMatchResult().getAwayTeamSetsWon() == 1) ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 3);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);

            } else if ( game.getMatchResult().getHomeTeamSetsWon() == 3 && game.getMatchResult().getAwayTeamSetsWon() == 2 ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 2);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);
                awayTeam.getTeamStats().setLeaguePoints(awayTeam.getTeamStats().getLeaguePoints() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);
            } else if ( (game.getMatchResult().getHomeTeamSetsWon() == 0 || game.getMatchResult().getHomeTeamSetsWon() == 1) && game.getMatchResult().getAwayTeamSetsWon() == 3 ) {
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
