package org.rafalzajac.web.controllers;

import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.MatchResult;
import org.rafalzajac.domain.Round;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.MatchResultService;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.RoundService;
import org.rafalzajac.service.TeamService;
import org.rafalzajac.web.administration.CreateNewElement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminPanelController {

    private TeamService teamService;
    private MatchService matchService;
    private RoundService roundService;
    private MatchResultService matchResultService;

    public AdminPanelController(MatchService matchService, RoundService roundService, MatchResultService matchResultService, TeamService teamService) {
        this.matchService = matchService;
        this.roundService = roundService;
        this.matchResultService = matchResultService;
        this.teamService = teamService;
    }

    @GetMapping("/admin")
    public String adminPanel() {
        return "administration/adminPanel";
    }

    @GetMapping("/creatematch")
    public String createTeam(Model model, @ModelAttribute("newMatch") Match match) {


        List<Round> rounds = roundService.findAllRounds();
        List<Team> teams = teamService.findAllTeams();

        model.addAttribute("roundSelect", rounds);
        model.addAttribute("allTeams", teams);


        return "administration/createMatch";
    }

    @PostMapping("/creatematch")
    public String processTeam(@ModelAttribute("currentMatch") Match match, Model model, @RequestParam("homeTeam") String homeTeam, @RequestParam("awayTeam")String awayTeam) {

        CreateNewElement createNewElement = new CreateNewElement(matchResultService, matchService, roundService, teamService);
        createNewElement.addNewMatch(match);
        System.out.println("Selected team is : " + homeTeam);

        return "redirect:/";
    }

}
