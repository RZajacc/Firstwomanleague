package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.fileProcessing.ProcessMatchResult;
import org.rafalzajac.web.fileProcessing.ScoutFileProcess;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "web/src/main/resources/static/scouts/";


    @GetMapping("/round")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        List<Match> matchList = matchService.findAllMatches();
        model.addAttribute("rounds", rounds);
        model.addAttribute("matches", matchList);

        return "views/round";
    }


    @PostMapping("/round")
    public String singleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long id) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        } else{
                try {

                    Optional<Match> match = matchService.getMatchById(id);
                    if (match.isPresent()) {

                        Match current = match.get();

                        if (!Files.exists(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName()))) {
                            Files.createDirectory(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName()));
                        }

                        if (!Files.exists(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName(), current.getRound().getLeague().getSeason()))) {
                            Files.createDirectory(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName(), current.getRound().getLeague().getSeason()));
                        }

                        Path path = Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName(),
                                current.getRound().getLeague().getSeason(), "R" + current.getRound().getRoundNumber() + "M" +
                                        current.getMatchNumber() + "_" + current.getHomeTeam() + "-" + current.getAwayTeam() + ".dvw");

                        Files.write(path, file.getBytes());


                        redirectAttributes.addFlashAttribute("message", "Successfully added. File was named :" +
                                current.getRound().getRoundNumber() + "M" + current.getMatchNumber() + "_" + current.getAwayTeam() +
                                "-" + current.getAwayTeam() + ".dvw");

                        current.setScoutPath(path.toString());
                        matchService.addMatch(current);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return "redirect:round/";
    }


    @GetMapping("/round/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws Exception{

        Optional<Match> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Match currentMatch = match.get();
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

    @PostMapping("/round/{id}")
    public  String matchSave(Model model, @RequestParam Long id, RedirectAttributes redirectAttributes) throws Exception {

        Optional<Match> selectedMatch = matchService.getMatchById(id);
        if (selectedMatch.isPresent()) {
            Match currentMatch = selectedMatch.get();
            model.addAttribute("currentMatch", currentMatch);
            if (currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(Paths.get(currentMatch.getScoutPath()), teamService, playerService, playerStatsService, teamStatsService);
                scoutFileProcess.processScoutFile();
                scoutFileProcess.saveStatsToDatabase();
                System.out.println("Current match result" + currentMatch.getMatchResult());
                redirectAttributes.addFlashAttribute("message", "All statistics saved properly!");
            } else {
                redirectAttributes.addFlashAttribute("message", "There is no scout file available for this match!");
            }
        }

    return "redirect:/round/" + id;
    }

    @GetMapping("/round/result/{id}")
    public String matchResultInfo(@PathVariable Long id, Model model) {

        Optional<Match> match = matchService.getMatchById(id);
        if (match.isPresent()) {
            Match current = match.get();
            model.addAttribute("currentMatch", current);
        }


        return "views/matchresult";
    }

    @PostMapping("/round/result/{id}")
    public String updateMatchResult(@ModelAttribute("currentMatch") Match match){

        ProcessMatchResult processMatchResult = new ProcessMatchResult(teamService, matchService);
        processMatchResult.addMatchResult(match);

        return "redirect:/round/" + match.getId();
    }


    @GetMapping("/table")
    public String leagueTable(Model model) {

        List<Team> teams = teamService.findAllTeams();

        Comparator<Team> comparator = (t1, t2) -> t2.getTeamStats().getLeaguePoints() - t1.getTeamStats().getLeaguePoints();
        comparator.thenComparing(team -> team.getTeamStats().getSetRatio());
//        comparator.thenComparing(team -> team.getTeamStats().getMatchWon());
//
//        comparator.thenComparing(team -> team.getTeamStats().getPointsRatio()).reversed();

        Stream<Team> teamStream = teams.stream().sorted(comparator);
        List<Team> sortedTeams = teamStream.collect(Collectors.toList());

//        teams.sort( (t1, t2) -> t2.getTeamStats().getLeaguePoints() - t1.getTeamStats().getLeaguePoints());
        model.addAttribute("allTeams", sortedTeams);

            return "views/table";
    }

    @GetMapping("/rank")
    public String rankTable(Model model) {

        List<Player> players = playerService.findAllPlayers();
        players.sort((p1, p2) ->
            p2.getPlayerStats().getPointsTotal() - p1.getPlayerStats().getPointsTotal()
        );

        model.addAttribute("allPlayers", players);

        return "views/rank";
    }

    @PostMapping("/rank")
    public String sortedRankTable (Model model, @RequestParam("selectBy") String text ) {
        System.out.println(text);

            List<Player> players = playerService.findAllPlayers();
            if (text.equals("alphabetically")){
                players.sort(Comparator.comparing(Player::getLastName));
            }else if (text.equals("club")){
                players.sort(Comparator.comparing(p -> p.getTeam().getTeamName()));
            } else if (text.equals("pointsTotal")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getPointsTotal() - p1.getPlayerStats().getPointsTotal()
                );
            } else if (text.equals("serveAce")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getServeAce() - p1.getPlayerStats().getServeAce()
                );
            }else if (text.equals("attackAttempt")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getAttackAttempts() - p1.getPlayerStats().getAttackAttempts()
                );
            }else if (text.equals("attackFinished")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getAttackFinished() - p1.getPlayerStats().getAttackFinished()
                );
            }else if (text.equals("attackFinishedPercent")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getAttackFinishedPercent() - p1.getPlayerStats().getAttackFinishedPercent()
                );
            }else if (text.equals("receptionAttempts")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getReceptionAttempts() - p1.getPlayerStats().getReceptionAttempts()
                );
            }else if (text.equals("receptionPositive")){
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getReceptionPositivePercent() - p1.getPlayerStats().getReceptionPositivePercent()
                );
            }else if (text.equals("receptrionPerfect")) {
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getReceptionPerfectPercent() - p1.getPlayerStats().getReceptionPerfectPercent()
                );
            }else {
                players.sort((p1, p2) ->
                        p2.getPlayerStats().getBlockScore() - p1.getPlayerStats().getBlockScore()
                );
            }

            model.addAttribute("allPlayers", players);

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
