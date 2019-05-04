package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainController {

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

        Optional<League> league = leagueService.getLeagueById(1L);

        if(league.isPresent()){
            League currentLeague = league.get();
            model.addAttribute("league",currentLeague);
        }

        //TODO list


        //Stworz interfejs do dodawania wszystkiego
        //Scieżki powinny być w kontrolerze jako path variable
        //Na tym etapie chyba trzeba będzie już łączyć tabele


        //Przemyslenia
        //Pomysl na samymi encjami, czy na pewno taki schemat zadziała przy meczach i dodawaniu ich do bazy danych
        //Być może oddziel statystyki od zawodnika (nowa klasa w domain, repo, service)
        //Liga, runda, mecz tworzony przez uzytkownika, uzupelniany przez scout
        //Czyli na pewno musi być link do strony meczu
        //I pod tym linkiem dodanie pliku scout z danymi i cały update, chyba że uda się takie coś z guzika przy liście
        //Folder w którym będa dane scouta powinien miec podfolder z sezonem


        return "MainPage/round";
    }
}
