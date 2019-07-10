package org.rafalzajac.service.bootstrap;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


//@Component
public class DataBaseLoader implements CommandLineRunner {

    private LeagueService leagueService;
    private RoundService roundService;
    private TeamService teamService;
    private TeamStatsService teamStatsService;
    private static final int NUMBER_OF_TEAMS = 14;

    public DataBaseLoader(LeagueService leagueService, RoundService roundService, TeamService teamService, TeamStatsService teamStatsService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.teamService = teamService;
        this.teamStatsService = teamStatsService;
    }



    @Override
    public void run(String... args) throws Exception {


        //Creating and save league instance

        League firstWoman = new League("1LK", "2018-2019");
        leagueService.addLeague(firstWoman);

        //Generating all rounds from league and saving its instances
        int roundsNumber = (NUMBER_OF_TEAMS - 1) * 2;

        for (int i = 0; i < roundsNumber; i++) {
            Round round = new Round(i + 1, firstWoman);
            roundService.addRound(round);
        }

        // Creating team stats instances. They are necessary to create team
        TeamStats team1Stats = new TeamStats();
        TeamStats team2Stats = new TeamStats();
        TeamStats team3Stats = new TeamStats();
        TeamStats team4Stats = new TeamStats();
        TeamStats team5Stats = new TeamStats();
        TeamStats team6Stats = new TeamStats();
        TeamStats team7Stats = new TeamStats();
        TeamStats team8Stats = new TeamStats();
        TeamStats team9Stats = new TeamStats();
        TeamStats team10Stats = new TeamStats();
        TeamStats team11Stats = new TeamStats();
        TeamStats team12Stats = new TeamStats();
        TeamStats team13Stats = new TeamStats();
        TeamStats team14Stats = new TeamStats();


        //Creating team instances
        Team team1 = new Team("JOK", "Joker Mekro Energoremont Swiecie", "Marcin Wojtowicz", "Milosz Szwaba", team1Stats);
        Team team2 = new Team("WIS", "Wisla Warszawa", "Piotr Hilko", "Mikolaj Mariaskin", team2Stats);
        Team team3 = new Team("MKS", "MKS Dabrowa Gornicza", "Adam Grabowski", "Ireneusz Borzecki", team3Stats);
        Team team4 = new Team("PWSZ", "PWSZ Tarnow", "Michal Betleja", "Michal Madejski", team4Stats);
        Team team5 = new Team("UNI", "AZS Uni Opole", "Nicola Vettori", "Akis Efstathopoulos", team5Stats);
        Team team6 = new Team("ENE", "Energetyk Poznan", "Michal Patyk", "Dominik Hajduk", team6Stats);
        Team team7 = new Team("SOL", "7R Solna Wieliczka", "Ryszard Litwin", "Marcin Nowakowski", team7Stats);
        Team team8 = new Team("GLI", "KS AZS Politechniki Slaskiej Gliwice", "Krzysztof Czapla", "Wojciech Czapla", team8Stats);
        Team team9 = new Team("TOR", "Budowlani Torun", "Miroslaw Zawieracz", team9Stats);
        Team team10 = new Team("KRO", "Karpaty PWSZ MOSiR Krosno Glass", "Dominik Stanislawczyk", "Tomasz Podulka", team10Stats);
        Team team11 = new Team("MIE", "UKS Szostka Mielec", "Roman Murdza", "Krystian Pachlinski", team11Stats);
        Team team12 = new Team("WLO", "WTS KDBS Bank Wloclawek", "Marek Zacharek", "Kazimierz Mendala", team12Stats);
        Team team13 = new Team("MAZ", "BlueSoft Mazovia Warszawa", "Robert Kupisz", "Kamil Trzcinski", team13Stats);
        Team team14 = new Team("SMS", "SMS PZPS Szczyrk", "Waldemar Kawka", "Ireneusz Waleczek", team14Stats);


        //Adding team stats to database
        teamStatsService.saveTeamStats(team1Stats);
        teamStatsService.saveTeamStats(team2Stats);
        teamStatsService.saveTeamStats(team3Stats);
        teamStatsService.saveTeamStats(team4Stats);
        teamStatsService.saveTeamStats(team5Stats);
        teamStatsService.saveTeamStats(team6Stats);
        teamStatsService.saveTeamStats(team7Stats);
        teamStatsService.saveTeamStats(team8Stats);
        teamStatsService.saveTeamStats(team9Stats);
        teamStatsService.saveTeamStats(team10Stats);
        teamStatsService.saveTeamStats(team11Stats);
        teamStatsService.saveTeamStats(team12Stats);
        teamStatsService.saveTeamStats(team13Stats);
        teamStatsService.saveTeamStats(team14Stats);


        //Adding teams to database
        teamService.addTeam(team1);
        teamService.addTeam(team2);
        teamService.addTeam(team3);
        teamService.addTeam(team4);
        teamService.addTeam(team5);
        teamService.addTeam(team6);
        teamService.addTeam(team7);
        teamService.addTeam(team8);
        teamService.addTeam(team9);
        teamService.addTeam(team10);
        teamService.addTeam(team11);
        teamService.addTeam(team12);
        teamService.addTeam(team13);
        teamService.addTeam(team14);
    }
}
