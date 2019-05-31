package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.administration.CreateNewElement;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private TeamService teamService;
    private MatchService matchService;
    private RoundService roundService;
    private MatchResultService matchResultService;
    private TeamStatsService teamStatsService;
    private PlayerService playerService;
    private PlayerStatsService playerStatsService;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "web/src/main/resources/static/scouts/";

    public AdminPanelController(MatchService matchService, RoundService roundService, MatchResultService matchResultService, TeamService teamService, TeamStatsService teamStatsService, PlayerService playerService, PlayerStatsService playerStatsService) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.matchResultService = matchResultService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
    }

    @GetMapping("/")
    public String adminPanel() {
        return "administration/adminPanel";
    }

    @GetMapping("/round-admin")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        List<Match> matchList = matchService.findAllMatches();
        model.addAttribute("rounds", rounds);

        return "administration/views/roundAdmin";
    }

    @PostMapping("/round-admin")
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

        return "redirect:/admin/round-admin/";
    }

    @GetMapping("/round-admin/{id}")
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

            return "administration/views/matchAdmin";
        }

        return "redirect:/";
    }

    @PostMapping("/round-admin/{id}")
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

        return "redirect:/admin/round-admin/" + id;
    }

    @GetMapping("/round-admin/result/{id}")
    public String matchResultInfo(@PathVariable Long id, Model model) {

        Optional<Match> match = matchService.getMatchById(id);
        if (match.isPresent()) {
            Match current = match.get();
            model.addAttribute("currentMatch", current);
        }


        return "administration/views/matchresult";
    }

    @PostMapping("/round-admin/result/{id}")
    public String updateMatchResult(@ModelAttribute("currentMatch") Match match){

        ProcessMatchResult processMatchResult = new ProcessMatchResult(teamService, matchService);
        processMatchResult.addMatchResult(match);

        return "redirect:/admin/round-admin/" + match.getId();
    }


    @GetMapping("/creatematch")
    public String createTeam(Model model, @ModelAttribute("newMatch") Match match) {


        List<Round> rounds = roundService.findAllRounds();
        List<Team> teams = teamService.findAllTeams();

        model.addAttribute("roundSelect", rounds);
        model.addAttribute("allTeams", teams);


        return "administration/createElements/createMatch";
    }

    @PostMapping("/creatematch")
    public String processMatch(@ModelAttribute("currentMatch") Match match, Model model, @RequestParam("homeTeam") String homeTeam, @RequestParam("awayTeam")String awayTeam) {

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService);
        createNewElement.addNewMatch(match);
        System.out.println("Selected team is : " + homeTeam);

        return "redirect:/round";
    }

    @GetMapping("/teams-admin")
    public  String teamData(Model model) {
        List<Team> allTeams = teamService.findAllTeams();
        model.addAttribute("teams", allTeams);
        return "administration/views/teamsAdmin";
    }


    @GetMapping("/createteam")
    public String createTeam(Model model, @ModelAttribute("newTeam") Team team) {

        return "administration/createElements/createTeam";
    }

    @PostMapping("/createteam")
    public String processTeam(@ModelAttribute("newTeam") Team team) {

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService);
        createNewElement.addNewTeam(team);

        return "redirect:/teams";
    }

}
