package org.rafalzajac.web.controllers;


import lombok.extern.slf4j.Slf4j;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.rafalzajac.web.administration.CreateNewElement;
import org.rafalzajac.web.fileProcessing.AmazonClient;
import org.rafalzajac.web.fileProcessing.ProcessMatchResult;
import org.rafalzajac.web.fileProcessing.ScoutFileProcess;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminPanelController {

    private TeamService teamService;
    private MatchService matchService;
    private RoundService roundService;
    private MatchResultService matchResultService;
    private TeamStatsService teamStatsService;
    private PlayerService playerService;
    private PlayerStatsService playerStatsService;

    //Amazon S3 bucket
    private AmazonClient amazonClient;


    public AdminPanelController(MatchService matchService, RoundService roundService, MatchResultService matchResultService, TeamService teamService, TeamStatsService teamStatsService, PlayerService playerService, PlayerStatsService playerStatsService, AmazonClient amazonClient) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.matchResultService = matchResultService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.amazonClient = amazonClient;
    }

    @GetMapping("/")
    public String adminPanel() {
        return "administration/adminPanel";
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
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        } else{
            try {

                Optional<Game> match = matchService.getMatchById(id);
                if (match.isPresent()) {


                    Game current = match.get();

                    String fileName = "R" + current.getRound().getRoundNumber() + "M" +
                            current.getMatchNumber() + "_" + current.getHomeTeam() + "-" + current.getAwayTeam() + ".dvw";
                    current.setScoutPath(fileName);
                    amazonClient.uploadFile(file, fileName);


                    redirectAttributes.addFlashAttribute("message", "Successfully added. File was named :" +
                            current.getRound().getRoundNumber() + "M" + current.getMatchNumber() + "_" + current.getAwayTeam() +
                            "-" + current.getAwayTeam() + ".dvw");


                    matchService.addMatch(current);
                }

            } catch (Exception e) {
               log.info("Exception occured", e);
            }
        }

        return "redirect:/admin/round-admin/";
    }

    @GetMapping("/round-admin/{id}")
    public  String matchInfo(@PathVariable Long id, Model model) throws Exception{

        Optional<Game> match = matchService.getMatchById(id);

        if(match.isPresent()) {
            Game currentMatch = match.get();
            model.addAttribute("currentMatch", currentMatch);


            //Now for file data
            if(currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, playerStatsService, teamStatsService, amazonClient);
                scoutFileProcess.processScoutFile();

                Team hTeam = teamService.getTeamByTeamName(currentMatch.getHomeTeam());
                if (hTeam.getTeamTag().equals(scoutFileProcess.getHomeTeam().getTeamTag())) {
                    model.addAttribute("homeTeam", scoutFileProcess.getHomeTeam());
                    model.addAttribute("awayTeam", scoutFileProcess.getAwayTeam());
                } else {
                    model.addAttribute("awayTeam", scoutFileProcess.getHomeTeam());
                    model.addAttribute("homeTeam", scoutFileProcess.getAwayTeam());
                }
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

            return "administration/views/matchAdmin";
        }

        return "redirect:/";
    }

    @PostMapping("/round-admin/{id}")
    public  String matchSave(Model model, @RequestParam Long id, RedirectAttributes redirectAttributes) throws Exception {

        Optional<Game> selectedMatch = matchService.getMatchById(id);
        if (selectedMatch.isPresent()) {
            Game currentMatch = selectedMatch.get();
            model.addAttribute("currentMatch", currentMatch);
            if (currentMatch.getScoutPath() != null) {
                ScoutFileProcess scoutFileProcess = new ScoutFileProcess(currentMatch.getScoutPath(), teamService, playerService, playerStatsService, teamStatsService, amazonClient);
                scoutFileProcess.processScoutFile();
                if(!currentMatch.getStatsSaved()){
                    scoutFileProcess.saveStatsToDatabase();
                    currentMatch.setStatsSaved(true);
                    matchService.addMatch(currentMatch);
                    redirectAttributes.addFlashAttribute("message", "All statistics saved properly!");
                } else {
                    redirectAttributes.addFlashAttribute("message", "Stats already saved!");
                }

                System.out.println("Current game result" + currentMatch.getMatchResult());

            } else {
                redirectAttributes.addFlashAttribute("message", "There is no scout file available for this game!");
            }
        }

        return "redirect:/admin/round-admin/" + id;
    }

    @PostMapping("/round-admin/deletematch")
    public String deleteMatch(@RequestParam("gameId") Long gameId) {
        matchService.deleteMatchById(gameId);
        return "redirect:/admin/round-admin/";
    }

    @GetMapping("/round-admin/result/{id}")
    public String matchResultInfo(@PathVariable Long id, Model model) {

        Optional<Game> match = matchService.getMatchById(id);
        if (match.isPresent()) {
            Game current = match.get();
            model.addAttribute("currentMatch", current);
        }


        return "administration/views/matchresult";
    }

    @PostMapping("/round-admin/result/{id}")
    public String updateMatchResult(@ModelAttribute("currentMatch") Game game){

//        ModelMapper modelMapper = new ModelMapper();
//        Game game = modelMapper.map(gameDTO, Game.class);

        ProcessMatchResult processMatchResult = new ProcessMatchResult(teamService, matchService, teamStatsService);
        processMatchResult.addMatchResult(game);

        return "redirect:/admin/round-admin/" + game.getId();
    }


    @GetMapping("/round-admin/creatematch")
    public String createMatch(Model model, @ModelAttribute("newMatch") Game game) {


        List<Round> rounds = roundService.findAllRounds();
        List<Team> teams = teamService.findAllTeams();

        model.addAttribute("roundSelect", rounds);
        model.addAttribute("allTeams", teams);


        return "administration/createElements/createMatch";
    }

    @PostMapping("/round-admin/creatematch")
    public String processMatch(@ModelAttribute("currentMatch") Game game, @RequestParam("homeTeam") String homeTeam, @RequestParam("awayTeam")String awayTeam) {

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        createNewElement.addNewMatch(game);
        System.out.println("Selected team is : " + homeTeam);

        return "redirect:/admin/round-admin";
    }

    @GetMapping("/teams-admin")
    public  String teamData(Model model) {
        List<Team> allTeams = teamService.findAllTeams();
        model.addAttribute("teams", allTeams);
        return "administration/views/teamsAdmin";
    }

    @GetMapping("/teams-admin/createteam")
    public String createTeam(Model model, @ModelAttribute("newTeam") Team team) {

        return "administration/createElements/createTeam";
    }

    @PostMapping("/teams-admin/createteam")
    public String processTeam(@ModelAttribute("newTeam") Team team) {

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        createNewElement.addNewTeam(team);

        return "redirect:/admin/teams-admin/";
    }



    @GetMapping("/teams-admin/currentteam-admin/{id}")
    public String currentTeam(@PathVariable Long id, @ModelAttribute("newPlayer")Player player, Model model){

        Optional<Team> team = teamService.getTeamById(id);

        if (team.isPresent()) {
            Team currentTeam = team.get();
            currentTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
            model.addAttribute("selectedTeam", currentTeam);
            return "administration/views/selectedTeamAdmin";
        }

        return "redirect:/admin/";
    }

    @PostMapping("/teams-admin/currentteam-admin/{id}")
    public String addPlayerToTeam(@RequestParam Long id, @ModelAttribute("newPlayer")Player player, Model model){

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        Optional<Team> team = teamService.getTeamById(id);

        if (team.isPresent()) {
            Team currentTeam = team.get();
            createNewElement.addNewPlayer(player, currentTeam);
        }


        return "redirect:/admin/teams-admin/currentteam-admin/" + id;
    }

    @PostMapping("/deletePlayer")
    public String deletePlayer(@RequestParam("playerId")Long playerId, @RequestParam("teamId") Long teamId) {

        playerService.deletePlayerById(playerId);

        return "redirect:/admin/teams-admin/currentteam-admin/" + teamId;
    }

    @GetMapping("/teams-admin/currentteam-admin/editplayer/{id}")
    public  String editPlayer (@PathVariable Long id, Model model) {

        Optional<Player> player = playerService.findPlayerById(id);
        if (player.isPresent()) {
            Player editPlayer = player.get();
            model.addAttribute("editPlayer", editPlayer);
        }

        return "administration/views/editPlayer";
    }

    @PostMapping("/teams-admin/currentteam-admin/editplayer/{id}")
    public String processEdit(@Valid @ModelAttribute("editPlayer")Player editPlayer, @RequestParam("teamId")Long teamId, BindingResult bindingResult) {

        System.out.println("EDIT PLAYER ID :" + editPlayer.getId());
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/teams-admin/currentteam-admin/editplayer/" + editPlayer.getId();
        }

        Optional<Player> player = playerService.findPlayerById(editPlayer.getId());
        if (player.isPresent()) {
            Player playerToEdit = player.get();
            playerToEdit.setNumber(editPlayer.getNumber());
            playerToEdit.setFirstName(editPlayer.getFirstName());
            playerToEdit.setLastName(editPlayer.getLastName());
            playerToEdit.setAge(editPlayer.getAge());
            playerToEdit.setHeight(editPlayer.getHeight());
            playerToEdit.setPosition(editPlayer.getPosition());
            playerService.addPlayer(playerToEdit);
        }

        return "redirect:/admin/teams-admin/currentteam-admin/" + teamId;
    }

    @GetMapping("teams-admin/currentteam-admin/editteam/{id}")
    public String editTeams(@PathVariable Long id, Model model){

        Optional<Team> team = teamService.getTeamById(id);
        if (team.isPresent()) {
            Team editTeam = team.get();
            model.addAttribute("editTeam", editTeam);
        }

        return "administration/views/editTeam";
    }

    @PostMapping("teams-admin/currentteam-admin/editteam/{id}")
    public String editTeamsProcess(@ModelAttribute("editTeam") Team editTeam){

        Optional<Team> team = teamService.getTeamById(editTeam.getId());
        if (team.isPresent()) {
            Team teamToEdit = team.get();
            teamToEdit.setTeamName(editTeam.getTeamName());
            teamToEdit.setTeamTag(editTeam.getTeamTag());
            teamToEdit.setFirstCoach(editTeam.getFirstCoach());
            teamToEdit.setSecondCoach(editTeam.getSecondCoach());
            teamToEdit.setWebPage(editTeam.getWebPage());
            teamToEdit.setFacebook(editTeam.getFacebook());
            teamService.addTeam(teamToEdit);
        }


        return "redirect:/admin/teams-admin";
    }



}
