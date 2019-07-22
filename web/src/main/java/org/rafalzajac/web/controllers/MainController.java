package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.file_processing.AmazonClient;
import org.rafalzajac.web.file_processing.ScoutFileProcess;
import org.rafalzajac.web.file_processing.SortData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;


@Controller
public class MainController {

        private NewsService newsService;
        private RoundService roundService;
        private GameService gameService;
        private TeamService teamService;
        private PlayerService playerService;
        private AmazonClient amazonClient;
        private static final String SELECTED_ROUNDS = "rounds";

        public MainController(RoundService roundService, GameService gameService, TeamService teamService, PlayerService playerService, AmazonClient amazonClient, NewsService newsService) {
        this.roundService = roundService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.amazonClient = amazonClient;
        this.newsService = newsService;
        }


    @GetMapping("/")
    public String homePage(Model model) {

        List<News> newsList = newsService.findAllNews();
        newsList.sort(Comparator.comparing(News::getCreationDate).reversed());

        model.addAttribute("newsList", newsList);

        return "home";
    }

    @GetMapping("/news-page/{id}")
    public String newsPage(Model model, @PathVariable Long id) {

        Optional<News> news = newsService.findNewsById(id);
        if (news.isPresent()) {
            News newsToView = news.get();
            model.addAttribute("news", newsToView);
        }


        return "views/newsPage";
    }

    @GetMapping("/round")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        List<Round> sortedList = rounds.stream()
                .map(round -> {
                    if (round != null) {
                        round.getMatchList().sort(Comparator.nullsLast(naturalOrder()));
                    }
                    return round;
                }).sorted(nullsLast(naturalOrder()))
                .collect(Collectors.toList());

        model.addAttribute(SELECTED_ROUNDS, sortedList);

        return "views/round";
    }

    @PostMapping("/round")
    public String selectRound (Model model, @RequestParam("selectRound") String text ) {

        if(text.equals("allRounds")) {
            model.addAttribute(SELECTED_ROUNDS, roundService.findAllRounds());
        } else {
            model.addAttribute(SELECTED_ROUNDS, roundService.findRoundByRoundNumber((Integer.parseInt(text))));
        }

        return "views/round";
    }

    @GetMapping("/round/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws IOException {

        Optional<Game> match = gameService.getMatchById(id);

        if(match.isPresent()) {
            Game currentMatch = match.get();
            model.addAttribute("currentMatch", currentMatch);


            //Now for file data
            if(currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, amazonClient);
                scoutFileProcess.processScoutFile();

                model.addAttribute("homeTeam", scoutFileProcess.getHomeTeam());
                model.addAttribute("awayTeam", scoutFileProcess.getAwayTeam());
            } else {
                Team hTeam = currentMatch.getTeams().get(0);
                hTeam.setTeamStats(new TeamStats());
                hTeam.getPlayerList().forEach(player -> player.setPlayerStats(new PlayerStats()));
                Team aTeam = currentMatch.getTeams().get(1);
                aTeam.getPlayerList().forEach(player -> player.setPlayerStats(new PlayerStats()));
                aTeam.setTeamStats(new TeamStats());
                model.addAttribute("homeTeam", hTeam);
                model.addAttribute("awayTeam", aTeam);
            }

                return "views/match";
            }

        return "redirect:/";
    }

    @GetMapping("/table")
    public String leagueTable(Model model) {

        List<Team> teams = teamService.findAllTeams();
        SortData sortData = new SortData();

        model.addAttribute("allTeams", sortData.sortTeamTable(teams));

            return "views/table";
    }

    @GetMapping("/rank")
    public String rankTable(Model model) {

        List<Player> players = playerService.findAllPlayers();
        players.sort(Comparator.comparing(Player::getLastName));

        model.addAttribute("allPlayers", players);

        return "views/rank";
    }

    @PostMapping("/rank")
    public String sortedRankTable (Model model, @RequestParam("selectBy") String text ) {

            List<Player> players = playerService.findAllPlayers();
            SortData sortData = new SortData();

            model.addAttribute("allPlayers", sortData.sortPlayerRankTable(players, text));

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
                currentTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
                model.addAttribute("selectedTeam", currentTeam);
                return "views/selectedTeam";
            }

            return "redirect:/";
    }

    @GetMapping("/contact")
    public String contact() {
        return "views/contact";
    }
}
