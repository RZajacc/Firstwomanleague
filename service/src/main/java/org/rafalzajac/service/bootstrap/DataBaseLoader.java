package org.rafalzajac.service.bootstrap;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataBaseLoader implements CommandLineRunner {

    private LeagueService leagueService;
    private RoundService roundService;
    private TeamService teamService;
    private static final int NUMBER_OF_TEAMS = 14;

    public DataBaseLoader(LeagueService leagueService, RoundService roundService, TeamService teamService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.teamService = teamService;
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


        //Creating team instances
        Team team1 = new Team("JOK", "Joker Mekro Energoremont Świecie", "Marcin Wojtowicz", "Miłosz Szwaba");
        Team team2 = new Team("WIS", "Wisła Warszawa", "Piotr Hilko", "Mikołaj Mariaskin");
        Team team3 = new Team("MKS", "MKS Dąbrowa Górnicza", "Adam Grabowski", "Ireneusz Borzęcki");
        Team team4 = new Team("PWSZ", "PWSZ Tarnów", "Michał Betleja", "Michał Madejski");
        Team team5 = new Team("UNI", "AZS Uni Opole", "Nicola Vettori", "Akis Efstathopoulos");
        Team team6 = new Team("ENE", "Energetyk Poznań", "Michał Patyk", "Dominik Hajduk");
        Team team7 = new Team("SOL", "7R Solna Wieliczka", "Ryszard Litwin", "Marcin Nowakowski");
        Team team8 = new Team("GLI", "KS AZS Politechniki Śląskiej Gliwice", "Krzysztof Czapla", "Wojciech Czapla");
        Team team9 = new Team("BUD", "Budowlani Toruń", "Mirosław Zawieracz");
        Team team10 = new Team("KRO", "Karpaty PWSZ MOSiR Krosno Glass", "Dominik Stanisławczyk", "Tomasz Podulka");
        Team team11 = new Team("MIE", "UKS Szóstka Mielec", "Roman Murdza", "Krystian Pachliński");
        Team team12 = new Team("WLO", "WTS KDBS Bank Włoclawek", "Marek Zacharek", "Kazimierz Mendala");
        Team team13 = new Team("MAZ", "BlueSoft Mazovia Warszawa", "Robert Kupisz", "Kamil Trzciński");
        Team team14 = new Team("SMS", "SMS PZPS Szczyrk", "Waldemar Kawka", "Ireneusz Waleczek");



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
