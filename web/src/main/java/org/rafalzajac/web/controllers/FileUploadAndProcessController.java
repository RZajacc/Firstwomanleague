package org.rafalzajac.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.file_processing.AmazonClient;
import org.rafalzajac.web.file_processing.ScoutFileProcess;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@Slf4j
public class FileUploadAndProcessController {

    private RoundService roundService;
    private MatchService matchService;
    private AmazonClient amazonClient;
    private TeamService teamService;
    private TeamStatsService teamStatsService;
    private PlayerService playerService;
    private PlayerStatsService playerStatsService;
    private static final String FLASH_MESSAGE = "message";
    private static final String REDIRECT_ROUND = "redirect:/admin/round-admin/";
    private static final String CURRENT_MATCH = "currentMatch";
    private static final String HOME_TEAM = "homeTeam";
    private static final String AWAY_TEAM = "awayTeam";

    public FileUploadAndProcessController(RoundService roundService, MatchService matchService, AmazonClient amazonClient, TeamService teamService, TeamStatsService teamStatsService, PlayerService playerService, PlayerStatsService playerStatsService) {
        this.roundService = roundService;
        this.matchService = matchService;
        this.amazonClient = amazonClient;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
    }

    @GetMapping("/round-admin")
    public String league(Model model) {

        List<Round> rounds = roundService.findAllRounds();
        model.addAttribute("rounds", rounds);

        return "administration/views/roundAdmin";
    }

    @PostMapping("/round-admin")
    public String singleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long id) {


        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Wybierz odpowiedni plik");
        } else{
            try {

                Optional<Game> match = matchService.getMatchById(id);
                if (match.isPresent()) {


                    Game current = match.get();

                    String fileName = "R" + current.getRound().getRoundNumber() + "M" +
                            current.getMatchNumber() + "_" + current.getHomeTeam() + "-" + current.getAwayTeam() + ".dvw";
                    current.setScoutPath(fileName);
                    amazonClient.uploadFile(file, fileName);


                    redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Pomyslnie dodano plik o nazwie :" +
                            current.getRound().getRoundNumber() + "M" + current.getMatchNumber() + "_" + current.getAwayTeam() +
                            "-" + current.getAwayTeam() + ".dvw");


                    matchService.addMatch(current);
                }

            } catch (Exception e) {
                log.info("Exception occured", e);
            }
        }

        return REDIRECT_ROUND;
    }


    @GetMapping("/round-admin/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws IOException {

        Optional<Game> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Game currentMatch = match.get();
            model.addAttribute(CURRENT_MATCH, currentMatch);


            //Now for file data
            if(currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, playerStatsService, teamStatsService, amazonClient);
                scoutFileProcess.processScoutFile();

                Team hTeam = teamService.getTeamByTeamName(currentMatch.getHomeTeam());
                if (hTeam.getTeamTag().equals(scoutFileProcess.getHomeTeam().getTeamTag())) {
                    model.addAttribute(HOME_TEAM, scoutFileProcess.getHomeTeam());
                    model.addAttribute(AWAY_TEAM, scoutFileProcess.getAwayTeam());
                } else {
                    model.addAttribute(AWAY_TEAM, scoutFileProcess.getHomeTeam());
                    model.addAttribute(HOME_TEAM, scoutFileProcess.getAwayTeam());
                }
            } else {
                Team hTeam = currentMatch.getTeams().get(0);
                hTeam.setTeamStats(new TeamStats());
                hTeam.getPlayerList().forEach(player -> player.setPlayerStats(new PlayerStats()));
                Team aTeam = currentMatch.getTeams().get(1);
                aTeam.getPlayerList().forEach(player -> player.setPlayerStats(new PlayerStats()));
                aTeam.setTeamStats(new TeamStats());
                model.addAttribute(HOME_TEAM, hTeam);
                model.addAttribute(AWAY_TEAM, aTeam);
            }

            return "administration/views/matchAdmin";
        }

        return "redirect:/";
    }

    @PostMapping("/round-admin/{id}")
    public  String matchSave(Model model, @RequestParam Long id, RedirectAttributes redirectAttributes) throws IOException {

        Optional<Game> selectedMatch = matchService.getMatchById(id);
        if (selectedMatch.isPresent()) {
            Game currentMatch = selectedMatch.get();
            model.addAttribute(CURRENT_MATCH, currentMatch);
            if (currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, playerStatsService, teamStatsService, amazonClient);
                scoutFileProcess.processScoutFile();
                if(!currentMatch.getStatsSaved()){
                    scoutFileProcess.saveStatsToDatabase();
                    currentMatch.setStatsSaved(true);
                    matchService.addMatch(currentMatch);
                    redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Dane zapisane prawidlowo!");
                } else {
                    redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Statystyki sa juz zapisane!");
                }

            } else {
                redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Plik scouta nie został jeszcze dodany!");
            }
        }

        return REDIRECT_ROUND + id;
    }
}
