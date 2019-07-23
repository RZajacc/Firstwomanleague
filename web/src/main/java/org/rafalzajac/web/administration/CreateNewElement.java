package org.rafalzajac.web.administration;

import lombok.NoArgsConstructor;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;


@NoArgsConstructor
public class CreateNewElement {

    private GameService gameService;
    private RoundService roundService;
    private TeamService teamService;
    private PlayerService playerService;

    public CreateNewElement(GameService gameService, RoundService roundService, TeamService teamService, PlayerService playerService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public void addNewMatch(Game game) {


        //Setting round
        game.setRound(roundService.findRoundByRoundNumber(game.getRound().getRoundNumber()));

        //Adding current game as played by both teams
        Team hTeam = teamService.getTeamByTeamName(game.getHomeTeam());
        Team aTeam = teamService.getTeamByTeamName(game.getAwayTeam());
        hTeam.getMatchList().add(game);
        aTeam.getMatchList().add(game);

        //Saving game and updating teams
        gameService.addMatch(game);
        teamService.addTeam(hTeam);
        teamService.addTeam(aTeam);
    }

    public void addNewTeam(Team team) {

        teamService.addTeam(team);
    }

    public void addNewPlayer(Player player, Team team) {

        //Assigning player to team
        player.setTeam(team);

        //Saving player
        playerService.addPlayer(player);

    }


}
