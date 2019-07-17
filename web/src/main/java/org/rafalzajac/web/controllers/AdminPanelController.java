package org.rafalzajac.web.controllers;


import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.rafalzajac.domain.*;
import org.rafalzajac.dto_objects.GameDTO;
import org.rafalzajac.dto_objects.PlayerDTO;
import org.rafalzajac.dto_objects.TeamDTO;
import org.rafalzajac.service.*;
import org.rafalzajac.web.administration.CreateNewElement;
import org.rafalzajac.web.file_processing.ProcessMatchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    private NewsService newsService;
    private static final String REDIRECT_CURRENT_TEAM = "redirect:/admin/teams-admin/currentteam-admin/";
    private static final String REDIRECT_ROUND = "redirect:/admin/round-admin/";
    private static final String FLASH_MESSAGE = "message";
    private static final String CURRENT_MATCH = "currentMatch";



    public AdminPanelController(MatchService matchService, RoundService roundService, MatchResultService matchResultService, TeamService teamService, TeamStatsService teamStatsService, PlayerService playerService, PlayerStatsService playerStatsService, NewsService newsService) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.matchResultService = matchResultService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String adminpanelNewsPage() {
        return "administration/adminPanel";
    }

    @GetMapping ("/create-article")
    public String createArticle (@ModelAttribute("newArticle") News news) {


        return "administration/createElements/createArticle";
    }

    @PostMapping("/create-article")
    public String saveArticle(@ModelAttribute("newArticle") News news) {

        newsService.saveNewsToDatabase(news);

        return "redirect:/admin/";
    }


    @GetMapping("/round-admin/creatematch")
    public String createMatchView(Model model, @ModelAttribute("newMatch") GameDTO game) {

        List<Round> rounds = roundService.findAllRounds();
        List<Team> teams = teamService.findAllTeams();

        model.addAttribute("roundSelect", rounds);
        model.addAttribute("allTeams", teams);


        return "administration/createElements/createMatch";
    }

    @PostMapping("/round-admin/creatematch")
    public String createMatch(@ModelAttribute("currentMatch") GameDTO game) {

        ModelMapper modelMapper = new ModelMapper();
        Game gameToPersist = modelMapper.map(game, Game.class);
        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        createNewElement.addNewMatch(gameToPersist);

        return "redirect:/admin/round-admin";
    }

    @PostMapping("/round-admin/deletematch")
    public String deleteMatch(@RequestParam("gameId") Long gameId) {
        matchService.deleteMatchById(gameId);
        return REDIRECT_ROUND;
    }


    @GetMapping("/round-admin/result/{id}")
    public String matchResultInfo(@PathVariable Long id, Model model) {

        Optional<Game> match = matchService.getMatchById(id);
        if (match.isPresent()) {
            Game current = match.get();
            model.addAttribute(CURRENT_MATCH, current);
        }

        return "administration/views/matchresult";
    }

    @PostMapping("/round-admin/result/{id}")
    public String updateMatchResult(@ModelAttribute("currentMatch") GameDTO game, RedirectAttributes redirectAttributes){

        ModelMapper modelMapper = new ModelMapper();
        Game gameToUpdate = modelMapper.map(game, Game.class);

        Optional<Game> game1 = matchService.getMatchById(game.getId());
        if (game1.isPresent()) {
            Game verifyResult = game1.get();
            if (verifyResult.getMatchResult().getHomeTeamSetsWon() == 0 && verifyResult.getMatchResult().getAwayTeamSetsWon() == 0){
                ProcessMatchResult processMatchResult = new ProcessMatchResult(teamService, matchService, matchResultService);
                processMatchResult.addMatchResult(gameToUpdate);
            }else {
                redirectAttributes.addFlashAttribute(FLASH_MESSAGE, "Nie można zmodyfikować wyniku!");
            }
        }

        return REDIRECT_ROUND + game.getId();
    }


    @GetMapping("/teams-admin")
    public  String teamsView(Model model) {
        List<Team> allTeams = teamService.findAllTeams();
        model.addAttribute("teams", allTeams);
        return "administration/views/teamsAdmin";
    }

    @GetMapping("/teams-admin/createteam")
    public String createTeamView(@ModelAttribute("newTeam") TeamDTO team) {

        return "administration/createElements/createTeam";
    }

    @PostMapping("/teams-admin/createteam")
    public String createTeam(@ModelAttribute("newTeam") TeamDTO team, Model model) {

        ModelMapper modelMapper = new ModelMapper();
        Team teamToPersist = modelMapper.map(team, Team.class);
        model.addAttribute("newTeam", new Team());
        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        createNewElement.addNewTeam(teamToPersist);

        return "redirect:/admin/teams-admin/";
    }


    @GetMapping("/teams-admin/currentteam-admin/{id}")
    public String currentTeam(@PathVariable Long id, @ModelAttribute("newPlayer") PlayerDTO player, Model model){

        Optional<Team> team = teamService.getTeamById(id);

        if (team.isPresent()) {
            Team currentTeam = team.get();
            currentTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
            model.addAttribute("selectedTeam", currentTeam);
            return "administration/views/selectedTeamAdmin";
        }

        return "redirect:/admin/";
    }

    @GetMapping("teams-admin/currentteam-admin/editteam/{id}")
    public String editTeamView(@PathVariable Long id, Model model){

        Optional<Team> team = teamService.getTeamById(id);
        if (team.isPresent()) {
            Team editTeam = team.get();
            model.addAttribute("editTeam", editTeam);
        }

        return "administration/views/editTeam";
    }

    @PostMapping("teams-admin/currentteam-admin/editteam/{id}")
    public String editTeam(@ModelAttribute("editTeam") TeamDTO editTeam){

        ModelMapper modelMapper = new ModelMapper();
        Team teamToPersist = modelMapper.map(editTeam, Team.class);

        Optional<Team> team = teamService.getTeamById(teamToPersist.getId());
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

    @PostMapping("/teams-admin/currentteam-admin/{id}")
    public String addPlayerToTeam(@RequestParam Long id, @ModelAttribute("newPlayer")PlayerDTO player){

        ModelMapper modelMapper = new ModelMapper();
        Player playerToPersist = modelMapper.map(player, Player.class);

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService, teamStatsService, playerStatsService, playerService);
        Optional<Team> team = teamService.getTeamById(id);

        if (team.isPresent()) {
            Team currentTeam = team.get();
            createNewElement.addNewPlayer(playerToPersist, currentTeam);
        }


        return REDIRECT_CURRENT_TEAM + id;
    }

    @PostMapping("/deletePlayer")
    public String deletePlayer(@RequestParam("playerId")Long playerId, @RequestParam("teamId") Long teamId) {

        playerService.deletePlayerById(playerId);

        return REDIRECT_CURRENT_TEAM + teamId;
    }

    @GetMapping("/teams-admin/currentteam-admin/editplayer/{id}")
    public  String editPlayerView (@PathVariable Long id, Model model) {

        Optional<Player> player = playerService.findPlayerById(id);
        if (player.isPresent()) {
            Player editPlayer = player.get();
            model.addAttribute("editPlayer", editPlayer);
        }

        return "administration/views/editPlayer";
    }

    @PostMapping("/teams-admin/currentteam-admin/editplayer/{id}")
    public String editPlayer(@Valid @ModelAttribute("editPlayer")PlayerDTO editPlayer, @RequestParam("teamId")Long teamId, BindingResult bindingResult) {

        ModelMapper modelMapper = new ModelMapper();
        Player playerToPersist = modelMapper.map(editPlayer, Player.class);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/teams-admin/currentteam-admin/editplayer/" + playerToPersist.getId();
        }

        Optional<Player> player = playerService.findPlayerById(playerToPersist.getId());
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

        return REDIRECT_CURRENT_TEAM + teamId;
    }

}
