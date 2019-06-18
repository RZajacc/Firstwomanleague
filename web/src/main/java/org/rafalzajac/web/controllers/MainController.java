package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.fileProcessing.ScoutFileProcess;
import org.rafalzajac.web.fileProcessing.SortData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Paths;
import java.util.*;


@Controller
public class MainController {

        private LeagueService leagueService;
        private RoundService roundService;
        private MatchService matchService;
        private TeamService teamService;
        private PlayerService playerService;
        private PlayerStatsService playerStatsService;
        private TeamStatsService teamStatsService;

        public MainController(LeagueService leagueService, RoundService roundService, MatchService matchService, TeamService teamService, PlayerService playerService, PlayerStatsService playerStatsService, TeamStatsService teamStatsService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.teamStatsService = teamStatsService;
        }



    @GetMapping("/round")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        model.addAttribute("rounds", rounds);

        return "views/round";
    }

    @PostMapping("/round")
    public String selectRound (Model model, @RequestParam("selectRound") String text ) {

        if(text.equals("allRounds")) {
            model.addAttribute("rounds", roundService.findAllRounds());
        } else {
            model.addAttribute("rounds", roundService.findRoundByRoundNumber((Integer.parseInt(text))));
        }

        return "views/round";
    }

    @GetMapping("/round/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws Exception{

        Optional<Game> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Game currentMatch = match.get();
            model.addAttribute("currentMatch", currentMatch);


            //Now for file data
            if(currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(Paths.get(currentMatch.getScoutPath()), teamService, playerService, playerStatsService, teamStatsService);
                scoutFileProcess.processScoutFile();

                model.addAttribute("homeTeam", scoutFileProcess.getHomeTeam());
                model.addAttribute("awayTeam", scoutFileProcess.getAwayTeam());
            } else {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess();
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
