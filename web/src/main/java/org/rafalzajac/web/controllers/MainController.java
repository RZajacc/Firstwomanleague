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

        List<Round> rounds = roundService.findAllRounds();
        List<Match> matchList = matchService.findAllMatches();
        model.addAttribute("rounds", rounds);
        model.addAttribute("matches", matchList);


        //TODO list


        //  Branch z danymi
        // 1. Dodaj do klasy mecz numer (String sciezka) i skoryguj konstrukor do tego oraz Command Line Runnera
        // 2. Zaimplementuj uplad plików i spróbuj przypisać ścieżkę do otwartego meczu
        // 3. Sprawdz czy to zadziałało
        // 4. Folder w którym będa dane scouta powinien miec podfolder z sezonem
        // 5. Dodaj do Command line runnera generator meczów, pomyśl nad algorytmem

        // Branch - z ligą i informacjami o niej
        // 1. Dodać fazę rozgrywek do ligi (Zasadnicza, Playoff, Playout?
        // 2. Generator kolejek? Tworząc ligę i podając liczbę zespołów stworzy od razu puste kolejki?
        // 3. Strona z dodawaniem meczów (Select z bazy danych?)
        // 4. Mecze powinny się wyświetlać z wszystkich kolejek (LSK) chyba, że wybierzesz konkretną kolejkę
        // 5. Wszystkie mecze jednego klubu?


        //Przemyslenia
        //Być może oddziel statystyki od zawodnika (nowa klasa w domain, repo, service)
        //Do statystyk dodaj pozycję na której zaczyna na boisku
        //Czy na pewno team musi mieć info o meczu w konstruktorze? -> Być może many do many (Rounda - mecze - zespoly?)

        return "views/round";
    }

    @GetMapping("/round/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) {
        Optional<Match> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Match currentMatch = match.get();
            model.addAttribute("currentMatch", currentMatch);
            return "views/match";
        }

        return "redirect:/";
    }

    @GetMapping("/table")
    public String leagueTable() {
            return "views/table";
    }

    @GetMapping("/rank")
    public String rankTable() {
        return "views/rank";
    }


    @GetMapping("/teams")
    public  String teamData(Model model) {

        List<Team> allTeams = teamService.findAllTeams();
        model.addAttribute("teams", allTeams);

        return "views/teams";
    }

    @GetMapping("/teams/currentteam/{id}")
    public String currentTeam(@PathVariable Long id, Model model){

            Optional<Team> team = teamService.getTeamById(id);

            if (team.isPresent()) {
                Team currentTeam = team.get();
                List<Player> players = currentTeam.getPlayerList();
                model.addAttribute("selectedTeam", currentTeam);
                model.addAttribute("teamPlayers", players);
                return "views/selectedTeam";
            }

            return "redirect:/";
    }

    @GetMapping("/contact")
    public String contact() {
        return "views/contact";
    }
}
