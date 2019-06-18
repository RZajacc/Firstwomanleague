package org.rafalzajac.web.administration;

import lombok.NoArgsConstructor;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;


@NoArgsConstructor
public class CreateNewElement {

    private MatchResultService matchResultService;
    private MatchService matchService;
    private RoundService roundService;
    private TeamService teamService;
    private TeamStatsService teamStatsService;
    private PlayerStatsService playerStatsService;
    private PlayerService playerService;

    public CreateNewElement(MatchResultService matchResultService, MatchService matchService, RoundService roundService, TeamService teamService, TeamStatsService teamStatsService, PlayerStatsService playerStatsService, PlayerService playerService) {
        this.matchResultService = matchResultService;
        this.matchService = matchService;
        this.roundService = roundService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
        this.playerStatsService = playerStatsService;
        this.playerService = playerService;
    }

    public void addNewMatch(Game game) {

        //Setting game result
        MatchResult newMatchResult = new MatchResult();
        matchResultService.saveMatchResult(newMatchResult);
        game.setMatchResult(newMatchResult);

        //Setting round
        game.setRound(roundService.findRoundByRoundNumber(game.getRound().getRoundNumber()));

        //Adding current game as played by both teams
        Team hTeam = teamService.getTeamByTeamName(game.getHomeTeam());
        Team aTeam = teamService.getTeamByTeamName(game.getAwayTeam());
        hTeam.getMatchList().add(game);
        aTeam.getMatchList().add(game);

        //Saving game and updating teams
        matchService.addMatch(game);
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

    public void addNewPlayer(Player player, Team team) {

        //Adding new player stats
        PlayerStats playerStats = new PlayerStats();
        playerStatsService.savePlayerStats(playerStats);

        //Setting player stats and team of a player
        player.setPlayerStats(playerStats);
        player.setTeam(team);

        //Saving player
        playerService.addPlayer(player);
    }


}
