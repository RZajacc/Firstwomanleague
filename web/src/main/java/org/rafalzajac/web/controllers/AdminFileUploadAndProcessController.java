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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminFileUploadAndProcessController {

    private RoundService roundService;
    private GameService gameService;
    private AmazonClient amazonClient;
    private TeamService teamService;
    private PlayerService playerService;
    private static final String FLASH_MESSAGE = "message";
    private static final String REDIRECT_ROUND = "redirect:/admin/round-admin/";
    private static final String CURRENT_MATCH = "currentMatch";
    private static final String HOME_TEAM = "homeTeam";
    private static final String AWAY_TEAM = "awayTeam";

    public AdminFileUploadAndProcessController(RoundService roundService, GameService gameService, AmazonClient amazonClient, TeamService teamService, PlayerService playerService) {
        this.roundService = roundService;
        this.gameService = gameService;
        this.amazonClient = amazonClient;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @GetMapping("/round-admin")
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
        model.addAttribute("rounds", sortedList);

        return "administration/views/roundAdmin";
    }

    @PostMapping("/round-admin")
    public String singleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long id) {


        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Wybierz odpowiedni plik");
        } else{
            try {

                Optional<Game> match = gameService.getMatchById(id);
                if (match.isPresent()) {


                    Game current = match.get();

                    String fileName = "R" + current.getRound().getRoundNumber() + "M" +
                            current.getMatchNumber() + "_" + current.getHomeTeam() + "-" + current.getAwayTeam() + ".dvw";
                    current.setScoutPath(fileName);
                    amazonClient.uploadFile(file, fileName);


                    redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Pomyslnie dodano plik o nazwie :" +
                            current.getRound().getRoundNumber() + "M" + current.getMatchNumber() + "_" + current.getAwayTeam() +
                            "-" + current.getAwayTeam() + ".dvw");


                    gameService.addMatch(current);
                }

            } catch (Exception e) {
                log.info("Exception occured", e);
            }
        }

        return REDIRECT_ROUND;
    }


    @GetMapping("/round-admin/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws IOException {

        Optional<Game> match = gameService.getMatchById(id);

        if(match.isPresent()) {
            Game currentMatch = match.get();
            model.addAttribute(CURRENT_MATCH, currentMatch);


            //Now for file data
            if(currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, amazonClient);
                scoutFileProcess.processScoutFile();

                Team hTeam = teamService.getTeamByTeamName(currentMatch.getHomeTeam());
                if (hTeam.getTeamTag().equals(scoutFileProcess.getHomeTeam().getTeamTag())) {
                    model.addAttribute(HOME_TEAM, scoutFileProcess.getHomeTeam());
                    model.addAttribute(AWAY_TEAM, scoutFileProcess.getAwayTeam());
                } else {
                    model.addAttribute(AWAY_TEAM, scoutFileProcess.getHomeTeam());
                    model.addAttribute(HOME_TEAM, scoutFileProcess.getAwayTeam());
                }
            }

            return "administration/views/matchAdmin";
        }

        return "redirect:/";
    }

    @PostMapping("/round-admin/{id}")
    public  String matchSave(Model model, @RequestParam Long id, RedirectAttributes redirectAttributes) throws IOException {

        Optional<Game> selectedMatch = gameService.getMatchById(id);
        if (selectedMatch.isPresent()) {
            Game currentMatch = selectedMatch.get();
            model.addAttribute(CURRENT_MATCH, currentMatch);
            if (currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, amazonClient);
                scoutFileProcess.processScoutFile();
                if(!currentMatch.getStatsSaved()){
                    scoutFileProcess.saveStatsToDatabase();
                    currentMatch.setStatsSaved(true);
                    gameService.addMatch(currentMatch);
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
