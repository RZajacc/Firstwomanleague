package org.rafalzajac.web.controllers;


import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
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
        List<Match> matchList = matchService.findAllMatches();

        model.addAttribute("matches", matchList);

        if(league.isPresent()){
            League currentLeague = league.get();
            model.addAttribute("league",currentLeague);
        }


        //TODO list

        // Na teraz
        // 1. Wybierz schemat boostrap do strony
        // 2. Stwórz zakładki home, mecze, tabela, kluby, rankingi, kontakt
        // 3. Wyśrodkuj zakładki na stronie
        // 4. Utwórz thymelaafowe fragmenty do nava, head, footer
        // 5. Zmien sciezkę strony meczu, samo id nic nie mowi, dodaj tam wiecej zmiennych plus id

        //  Branch z danymi
        // 1. Dodaj do klasy mecz numer (String sciezka) i skoryguj konstrukor do tego oraz Command Line Runnera
        // 2. Zaimplementuj uplad plików i spróbuj przypisać ścieżkę do otwartego meczu
        // 3. Sprawdz czy to zadziałało
        // 4. Folder w którym będa dane scouta powinien miec podfolder z sezonem

        // Branch - z ligą i informacjami o niej
        // 1. Dodać fazę rozgrywek do ligi (Zasadnicza, Playoff, Playout?
        // 2. Generator kolejek? Tworząc ligę i podając liczbę zespołów stworzy od razu puste kolejki?
        // 3. Strona z dodawaniem meczów (Select z bazy danych?)
        // 4. Mecze powinny się wyświetlać z wszystkich kolejek (LSK) chyba, że wybierzesz konkretną kolejkę
        // 5. Wszystkie mecze jednego klubu?


        //Przemyslenia
        //Być może oddziel statystyki od zawodnika (nowa klasa w domain, repo, service)
        //Do statystyk dodaj pozycję na której zaczyna na boisku

        return "MainPage/round";
    }

    @GetMapping("/round/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) {
        Optional<Match> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Match currentMatch = match.get();
            model.addAttribute("currentMatch", currentMatch);
            return "MainPage/match";
        }

        return "redirect:/";
    }
}
