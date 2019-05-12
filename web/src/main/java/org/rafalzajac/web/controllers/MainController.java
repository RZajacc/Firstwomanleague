package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
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

        return "views/round";
    }


    @PostMapping("/round")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam("id") Long id) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            Optional<Match> match = matchService.getMatchById(id);
            if(match.isPresent()) {

                Match current = match.get();

                if(!Files.exists(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName() ))) {
                    Files.createDirectory(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName()));
                }

                if(!Files.exists(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName(), current.getRound().getLeague().getSeason()))){
                    Files.createDirectory(Paths.get(UPLOADED_FOLDER, current.getRound().getLeague().getLeagueName(), current.getRound().getLeague().getSeason()));
                }

                Path path = Paths.get(UPLOADED_FOLDER,  current.getRound().getLeague().getLeagueName(),
                        current.getRound().getLeague().getSeason(), "R" + current.getRound().getRoundNumber()+ "M"+
                        current.getMatchNumber() + "_" + current.getHomeTeam() + "-" + current.getAwayTeam() + ".dvw");

                Files.write(path, file.getBytes());
                

                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'" + path.toString());

                current.setScoutPath(path.toString());
                matchService.addMatch(current);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
            ScoutFileProcess scoutFileProcess = new ScoutFileProcess(Paths.get(currentMatch.getScoutPath()));
            scoutFileProcess.processScoutFile();


            model.addAttribute("homeTeam", scoutFileProcess.getHomeTeam());
            model.addAttribute("awayTeam", scoutFileProcess.getAwayTeam());


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