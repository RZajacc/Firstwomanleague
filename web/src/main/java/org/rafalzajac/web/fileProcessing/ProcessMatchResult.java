package org.rafalzajac.web.fileProcessing;

import org.rafalzajac.domain.Game;
import org.rafalzajac.domain.Team;
import org.rafalzajac.domain.TeamStats;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.TeamService;
import org.rafalzajac.service.TeamStatsService;

import java.util.Optional;


public class ProcessMatchResult {

    private TeamService teamService;
    private MatchService matchService;
    private TeamStatsService teamStatsService;


    public ProcessMatchResult(TeamService teamService, MatchService matchService, TeamStatsService teamStatsService) {
        this.teamService = teamService;
        this.matchService = matchService;
        this.teamStatsService = teamStatsService;
    }

    public void addMatchResult (Game game) {

        Optional<Game> selected = matchService.getMatchById(game.getId());
        if(selected.isPresent()) {

            Game matchToUpdate = selected.get();

            //Sets won by both teams
            matchToUpdate.getMatchResult().setHomeTeamSetsWon(game.getMatchResult().getHomeTeamSetsWon());
            matchToUpdate.getMatchResult().setAwayTeamSetsWon(game.getMatchResult().getAwayTeamSetsWon());

            //Home team each set score
            matchToUpdate.getMatchResult().setHomeTeamSet1Score(game.getMatchResult().getHomeTeamSet1Score());
            matchToUpdate.getMatchResult().setHomeTeamSet2Score(game.getMatchResult().getHomeTeamSet2Score());
            matchToUpdate.getMatchResult().setHomeTeamSet3Score(game.getMatchResult().getHomeTeamSet3Score());
            matchToUpdate.getMatchResult().setHomeTeamSet4Score(game.getMatchResult().getHomeTeamSet4Score());
            matchToUpdate.getMatchResult().setHomeTeamSet5Score(game.getMatchResult().getHomeTeamSet5Score());

            //Away team each set score
            matchToUpdate.getMatchResult().setAwayTeamSet1Score(game.getMatchResult().getAwayTeamSet1Score());
            matchToUpdate.getMatchResult().setAwayTeamSet2Score(game.getMatchResult().getAwayTeamSet2Score());
            matchToUpdate.getMatchResult().setAwayTeamSet3Score(game.getMatchResult().getAwayTeamSet3Score());
            matchToUpdate.getMatchResult().setAwayTeamSet4Score(game.getMatchResult().getAwayTeamSet4Score());
            matchToUpdate.getMatchResult().setAwayTeamSet5Score(game.getMatchResult().getAwayTeamSet5Score());
            updateTeamStats(matchToUpdate);
            matchService.addMatch(matchToUpdate);
        }


    }


    public void updateTeamStats(Game game) {

        Team hTeam = game.getTeams().get(0);
        Team aTeam = game.getTeams().get(1);

        TeamStats homeTeam = hTeam.getTeamStats();
        TeamStats awayTeam =aTeam.getTeamStats();


        if(game.getMatchResult().getAwayTeamSetsWon() != 0 || game.getMatchResult().getHomeTeamSetsWon() != 0){

            //Setting matches played for both teams
            homeTeam.setMatchPlayed(homeTeam.getMatchPlayed() + 1);
            awayTeam.setMatchPlayed(awayTeam.getMatchPlayed() + 1);

            //Setting sets won and lost for both teams
            homeTeam.setSetsWon(homeTeam.getSetsWon() + game.getMatchResult().getHomeTeamSetsWon());
            homeTeam.setSetsLost(homeTeam.getSetsLost() + game.getMatchResult().getAwayTeamSetsWon());
            awayTeam.setSetsWon(awayTeam.getSetsWon() + game.getMatchResult().getAwayTeamSetsWon());
            awayTeam.setSetsLost(awayTeam.getSetsLost() + game.getMatchResult().getHomeTeamSetsWon());

            //Setting sets ratio for home team
            if (homeTeam.getSetsLost() == 0) {
                homeTeam.setSetRatio(homeTeam.getSetsWon());
            }else {
                homeTeam.setSetRatio( (float)homeTeam.getSetsWon() / homeTeam.getSetsLost());
            }

            //Setting sets ratio for away team
            if (awayTeam.getSetsLost() == 0) {
                awayTeam.setSetRatio(awayTeam.getSetsWon());
            }else {
                awayTeam.setSetRatio( (float)awayTeam.getSetsWon() / awayTeam.getSetsLost());
            }

            //Setting points scored by both teams
                homeTeam.setPointsWon(homeTeam.getPointsWon() + game.getMatchResult().getHomeTeamSet1Score() + game.getMatchResult().getHomeTeamSet2Score() + game.getMatchResult().getHomeTeamSet3Score() + game.getMatchResult().getHomeTeamSet4Score() + game.getMatchResult().getHomeTeamSet5Score());
                homeTeam.setPointsLost(homeTeam.getPointsLost() + game.getMatchResult().getAwayTeamSet1Score() + game.getMatchResult().getAwayTeamSet2Score() + game.getMatchResult().getAwayTeamSet3Score() + game.getMatchResult().getAwayTeamSet4Score() + game.getMatchResult().getAwayTeamSet5Score());
                homeTeam.setTeamPointsRatio((float) homeTeam.getPointsWon() / homeTeam.getPointsLost());
                awayTeam.setPointsWon(awayTeam.getPointsWon() + game.getMatchResult().getAwayTeamSet1Score() + game.getMatchResult().getAwayTeamSet2Score() + game.getMatchResult().getAwayTeamSet3Score() + game.getMatchResult().getAwayTeamSet4Score() + game.getMatchResult().getAwayTeamSet5Score());
                awayTeam.setPointsLost(awayTeam.getPointsLost() + game.getMatchResult().getHomeTeamSet1Score() + game.getMatchResult().getHomeTeamSet2Score() + game.getMatchResult().getHomeTeamSet3Score() + game.getMatchResult().getHomeTeamSet4Score() + game.getMatchResult().getHomeTeamSet5Score());
                awayTeam.setTeamPointsRatio((float) awayTeam.getPointsWon() / awayTeam.getPointsLost());

            //Setting league points, matches won and matches lost
            if( game.getMatchResult().getHomeTeamSetsWon() == 3 && (game.getMatchResult().getAwayTeamSetsWon() == 0 || game.getMatchResult().getAwayTeamSetsWon() == 1) ) {
                homeTeam.setLeaguePoints(homeTeam.getLeaguePoints() + 3);
                homeTeam.setMatchWon(homeTeam.getMatchWon() + 1);

                awayTeam.setMatchLost(awayTeam.getMatchLost() + 1);

            } else if ( game.getMatchResult().getHomeTeamSetsWon() == 3 && game.getMatchResult().getAwayTeamSetsWon() == 2 ) {
                homeTeam.setLeaguePoints(homeTeam.getLeaguePoints() + 2);
                homeTeam.setMatchWon(homeTeam.getMatchWon() + 1);
                awayTeam.setLeaguePoints(awayTeam.getLeaguePoints() + 1);

                awayTeam.setMatchLost(awayTeam.getMatchLost() + 1);
            } else if ( (game.getMatchResult().getHomeTeamSetsWon() == 0 || game.getMatchResult().getHomeTeamSetsWon() == 1) && game.getMatchResult().getAwayTeamSetsWon() == 3 ) {
                awayTeam.setLeaguePoints(awayTeam.getLeaguePoints() + 3);
                awayTeam.setMatchWon(awayTeam.getMatchWon() + 1);

                homeTeam.setMatchLost(homeTeam.getMatchLost() + 1);
            } else {
                awayTeam.setLeaguePoints(awayTeam.getLeaguePoints() + 2);
                awayTeam.setMatchWon(awayTeam.getMatchWon() + 1);
                homeTeam.setLeaguePoints(homeTeam.getLeaguePoints() + 1);

                homeTeam.setMatchLost(homeTeam.getMatchLost() + 1);
            }

            teamStatsService.saveTeamStats(homeTeam);
            teamStatsService.saveTeamStats(awayTeam);

            hTeam.setTeamStats(homeTeam);
            aTeam.setTeamStats(awayTeam);

            teamService.addTeam(hTeam);
            teamService.addTeam(aTeam);
        }
    }
}
