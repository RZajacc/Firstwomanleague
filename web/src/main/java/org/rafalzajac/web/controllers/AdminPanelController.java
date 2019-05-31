package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.Round;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.*;
import org.rafalzajac.web.administration.CreateNewElement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private TeamService teamService;
    private MatchService matchService;
    private RoundService roundService;
    private MatchResultService matchResultService;
    private TeamStatsService teamStatsService;

    public AdminPanelController(MatchService matchService, RoundService roundService, MatchResultService matchResultService, TeamService teamService, TeamStatsService teamStatsService) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.matchResultService = matchResultService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
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

    @GetMapping("/teams-admin")
    public  String teamData(Model model) {
        List<Team> allTeams = teamService.findAllTeams();
        model.addAttribute("teams", allTeams);
        return "administration/views/teamsAdmin";
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
