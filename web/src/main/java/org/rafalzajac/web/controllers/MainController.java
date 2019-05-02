package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.League;
import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.Round;
import org.rafalzajac.repository.LeagueRepository;
import org.rafalzajac.repository.MatchRepository;
import org.rafalzajac.repository.RoundRepository;
import org.rafalzajac.service.LeagueService;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.RoundService;
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

        public MainController(LeagueService leagueService, RoundService roundService, MatchService matchService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.matchService = matchService;
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

        leagueService.addLeague(FirstWoman);
        roundService.addRound(round1);
        matchService.addMatch(match1);
        matchService.addMatch(match2);
        matchService.addMatch(match3);
        matchService.addMatch(match4);
        matchService.addMatch(match5);

        model.addAttribute("league", FirstWoman);
        model.addAttribute("round", round1);
        model.addAttribute("match", match1);

        //TODO list
        //Uzupełnij w domain konstruktury tak żeby każdy uzupełniał danymi grupę do której należy
        //Utwórz podstawowe serwisy do każdego repozytorium
        //Przemysl metody jakie mialyby miec te serwisy, na pewno zapełnianie list wywołaniami z bazy żeby znaleźć
        //np wszystkie mecze danej rundy

        //Przemyslenia
        //Liga, runda, mecz tworzony przez uzytkownika, uzupelniany przez scout
        //Czyli na pewno musi być link do strony meczu
        //I pod tym linkiem dodanie pliku scout z danymi i cały update, chyba że uda się takie coś z guzika przy liście


        return "MainPage/round";
    }
}
