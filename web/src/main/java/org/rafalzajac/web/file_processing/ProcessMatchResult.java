package org.rafalzajac.web.file_processing;

import lombok.extern.slf4j.Slf4j;
import org.rafalzajac.domain.Game;
import org.rafalzajac.domain.GameResult;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.GameResultService;
import org.rafalzajac.service.GameService;
import org.rafalzajac.service.TeamService;

import java.util.Optional;

@Slf4j
public class ProcessMatchResult {

    private TeamService teamService;
    private GameService gameService;
    private GameResultService gameResultService;


    public ProcessMatchResult(TeamService teamService, GameService gameService, GameResultService gameResultService) {
        this.teamService = teamService;
        this.gameService = gameService;
        this.gameResultService = gameResultService;
    }

    public void addMatchResult (Game game) {

        Optional<Game> selected = gameService.getMatchById(game.getId());

        if(selected.isPresent()) {

            Game matchToUpdate = selected.get();
            GameResult gameResult = game.getGameResult();

            if (matchToUpdate.getGameResult().getHomeTeamSetsWon() == 0 && matchToUpdate.getGameResult().getAwayTeamSetsWon() == 0) {
                gameResultService.saveMatchResult(gameResult);
                matchToUpdate.setGameResult(gameResult);
                gameService.addMatch(matchToUpdate);
                addTeamStats(matchToUpdate);
            }
        }
    }


    public void addTeamStats(Game game) {

        Team homeTeam = game.getTeams().get(0);
        Team awayTeam = game.getTeams().get(1);


        //Setting matches played for both teams
        homeTeam.getTeamStats().setMatchPlayed(homeTeam.getTeamStats().getMatchPlayed() + 1);
        awayTeam.getTeamStats().setMatchPlayed(awayTeam.getTeamStats().getMatchPlayed() + 1);

        //Setting sets won and lost for both teams
        homeTeam.getTeamStats().setSetsWon(homeTeam.getTeamStats().getSetsWon() + game.getGameResult().getHomeTeamSetsWon());
        homeTeam.getTeamStats().setSetsLost(homeTeam.getTeamStats().getSetsLost() + game.getGameResult().getAwayTeamSetsWon());
        awayTeam.getTeamStats().setSetsWon(awayTeam.getTeamStats().getSetsWon() + game.getGameResult().getAwayTeamSetsWon());
        awayTeam.getTeamStats().setSetsLost(awayTeam.getTeamStats().getSetsLost() + game.getGameResult().getHomeTeamSetsWon());

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

            // Do poprawy!!! Jeżeli nie ma wyniku przynajmniej jednego seta dzieli 0/0 i wychodzi Nan!
                homeTeam.getTeamStats().setPointsWon(homeTeam.getTeamStats().getPointsWon() + game.getGameResult().getHomeTeamSet1Score() + game.getGameResult().getHomeTeamSet2Score() + game.getGameResult().getHomeTeamSet3Score() + game.getGameResult().getHomeTeamSet4Score() + game.getGameResult().getHomeTeamSet5Score());
                homeTeam.getTeamStats().setPointsLost(homeTeam.getTeamStats().getPointsLost() + game.getGameResult().getAwayTeamSet1Score() + game.getGameResult().getAwayTeamSet2Score() + game.getGameResult().getAwayTeamSet3Score() + game.getGameResult().getAwayTeamSet4Score() + game.getGameResult().getAwayTeamSet5Score());
                homeTeam.getTeamStats().setTeamPointsRatio((float) homeTeam.getTeamStats().getPointsWon() / homeTeam.getTeamStats().getPointsLost());
                awayTeam.getTeamStats().setPointsWon(awayTeam.getTeamStats().getPointsWon() + game.getGameResult().getAwayTeamSet1Score() + game.getGameResult().getAwayTeamSet2Score() + game.getGameResult().getAwayTeamSet3Score() + game.getGameResult().getAwayTeamSet4Score() + game.getGameResult().getAwayTeamSet5Score());
                awayTeam.getTeamStats().setPointsLost(awayTeam.getTeamStats().getPointsLost() + game.getGameResult().getHomeTeamSet1Score() + game.getGameResult().getHomeTeamSet2Score() + game.getGameResult().getHomeTeamSet3Score() + game.getGameResult().getHomeTeamSet4Score() + game.getGameResult().getHomeTeamSet5Score());
                awayTeam.getTeamStats().setTeamPointsRatio((float) awayTeam.getTeamStats().getPointsWon() / awayTeam.getTeamStats().getPointsLost());

            //Setting league points, matches won and matches lost
            if( game.getGameResult().getHomeTeamSetsWon() == 3 && (game.getGameResult().getAwayTeamSetsWon() == 0 || game.getGameResult().getAwayTeamSetsWon() == 1) ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 3);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);

            } else if ( game.getGameResult().getHomeTeamSetsWon() == 3 && game.getGameResult().getAwayTeamSetsWon() == 2 ) {
                homeTeam.getTeamStats().setLeaguePoints(homeTeam.getTeamStats().getLeaguePoints() + 2);
                homeTeam.getTeamStats().setMatchWon(homeTeam.getTeamStats().getMatchWon() + 1);
                awayTeam.getTeamStats().setLeaguePoints(awayTeam.getTeamStats().getLeaguePoints() + 1);

                awayTeam.getTeamStats().setMatchLost(awayTeam.getTeamStats().getMatchLost() + 1);
            } else if ( (game.getGameResult().getHomeTeamSetsWon() == 0 || game.getGameResult().getHomeTeamSetsWon() == 1) && game.getGameResult().getAwayTeamSetsWon() == 3 ) {
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
