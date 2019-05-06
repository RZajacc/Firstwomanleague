package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.UserTransaction;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "web/src/main/resources/static/scouts/";

    @GetMapping("/round")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        List<Match> matchList = matchService.findAllMatches();
        model.addAttribute("rounds", rounds);
        model.addAttribute("matches", matchList);



        //TODO list

        //  Branch z danymi (Wzoruj się na przykładzie Mykonga
        // 1. Folder w którym będa dane scouta powinien miec podfolder z sezonem
        // 2. Działa to dość pokracznie, ale jednak działa, pomyśl jak lepiej przekazać plik z formularza
        // 3. Wygeneruj scouta na stronie meczu ze ścieżki.
        // 4. Jeżeli to wszystko zadziała możesz po trochu zacząć dłubać z generowaniem statystyk


        // Branch - z ligą i informacjami o niej
        // 1. Dodać fazę rozgrywek do ligi (Zasadnicza, Playoff, Playout?)
        // 2. Pomyśl jak dodawać rundy i mecze do istniejącej ligi


        //Przemyslenia
        //Być może oddziel statystyki od zawodnika (nowa klasa w domain, repo, service)
        //Do statystyk dodaj pozycję na której zaczyna na boisku
        //Czy na pewno team musi mieć info o meczu w konstruktorze? -> Być może many do many (Rounda - mecze - zespoly?)
        //Wyszukiwanie wszystkich meczów oraz wybranej kolejki lub drużyny

        return "views/round";
    }

    //************************************************************
    @PostMapping("/round") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam("id") Long id) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            // A gdyby wszystko to dać do wybranego meczu i do ścieżki dopisać sezon z klasy?
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'" + path.toString());

            Optional<Match> match = matchService.getMatchById(id);
            if(match.isPresent()) {
                Match current = match.get();
                current.setScoutPath(path.toString());
                matchService.addMatch(current);
                System.out.println(current.getRound().getLeague().getLeagueName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:round/";
    }


    //********************************************************************

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
