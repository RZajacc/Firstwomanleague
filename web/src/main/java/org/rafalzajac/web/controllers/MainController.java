package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);


        private LeagueService leagueService;
        private RoundService roundService;
        private MatchService matchService;
        private TeamService teamService;
        private PlayerService playerService;

        public MainController(LeagueService leagueService, RoundService roundService, MatchService matchService, TeamService teamService, PlayerService playerService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
        }

    @GetMapping("/round")
    public String league(Model model) {
        League FirstWoman = new League("1LK", "2018/2019");

        Round round1 = new Round(1, FirstWoman);

        Match match1 = new Match("MKS Dabrowa", "PWSZ Tarnow", round1);
        Match match2 = new Match("Joker Swiecie", "MKS Dabrowa", round1);
        Match match3 = new Match("MKS Dabrowa", "Energetyk Poznan", round1);
        Match match4 = new Match("Uni Opole", "MKS Dabrowa", round1);
        Match match5 = new Match("MKS Dabrowa", "7R Solna Wieliczka", round1);

        Team team1 = new Team("MKS", "MKS Dabrowa", "Adam Grabowsk", "Irek Borzecki", match1);
        Team team2 = new Team("PWSZ", "PWSZ Tarnow", "Michal Betleja", "Michal Madejski", match1);

        Player player1 = new Player(1, "Kat-Bry", "Katarzyna", "Bryda", team1);
        Player player2 = new Player(2, "Col-Kam", "Kamila", "Colik", team1);
        Player player3 = new Player(3, "Zuz-Szy", "Zuzanna", "Szynkiel", team1);

        Player player4 = new Player(2, "Mag-Sza", "Magdalena", "Szabo", team2);
        Player player5 = new Player(5, "Mag-Pyt", "Magdalena", "Pytel", team2);
        Player player6 = new Player(8, "Mag-Jur", "Magdalena", "Jurczyk", team2);


        leagueService.addLeague(FirstWoman);

        roundService.addRound(round1);

        matchService.addMatch(match1);
        matchService.addMatch(match2);
        matchService.addMatch(match3);
        matchService.addMatch(match4);
        matchService.addMatch(match5);

        teamService.addTeam(team1);
        teamService.addTeam(team2);

        playerService.addPlayer(player1);
        playerService.addPlayer(player2);
        playerService.addPlayer(player3);
        playerService.addPlayer(player4);
        playerService.addPlayer(player5);
        playerService.addPlayer(player6);


        model.addAttribute("league", FirstWoman);
        model.addAttribute("round", round1);
        model.addAttribute("match", match1);

        //TODO list

        //Stworz command line runner
        //Stworz interfejs do dodawania wszystkiego


        //Przemyslenia
        //Liga, runda, mecz tworzony przez uzytkownika, uzupelniany przez scout
        //Czyli na pewno musi być link do strony meczu
        //I pod tym linkiem dodanie pliku scout z danymi i cały update, chyba że uda się takie coś z guzika przy liście


        return "MainPage/round";
    }
}
