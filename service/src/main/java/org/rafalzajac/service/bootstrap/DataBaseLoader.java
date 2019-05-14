package org.rafalzajac.service.bootstrap;

import org.rafalzajac.domain.*;
import org.rafalzajac.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataBaseLoader implements CommandLineRunner {

    private LeagueService leagueService;
    private RoundService roundService;
    private MatchService matchService;
    private TeamService teamService;
    private PlayerService playerService;

    public DataBaseLoader(LeagueService leagueService, RoundService roundService, MatchService matchService, TeamService teamService, PlayerService playerService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @Override
    public void run(String... args) throws Exception {

        //Creating league instance
        League FirstWoman = new League("1LK", "2018-2019");

        //Creating round instance
        Round round1 = new Round(1, FirstWoman);

        //Creating match instances
        Match match1 = new Match("MKS Dabrowa", "PWSZ Tarnow", 1, round1);
        Match match2 = new Match("Joker Swiecie", "MKS Dabrowa",2,  round1);
        Match match3 = new Match("MKS Dabrowa", "Energetyk Poznan", 3, round1);



        //Creating team instances
        Team team1 = new Team("MKS", "MKS Dąbrowa Górnicza", "Adam Grabowsk", "Ireneusz Borzęcki");
        Team team2 = new Team("PWSZ", "PWSZ Tarnow", "Michal Betleja", "Michal Madejski");
        Team team3 = new Team("JOK", "Joker Swiecie", "Michal Wojtowicz", "Milosz Szwaba");
        Team team4 = new Team("ENE", "Energetyk Poznan", "Michal Patyk");

        team1.getMatchList().add(match1);
        team1.getMatchList().add(match2);
        team1.getMatchList().add(match3);

        team2.getMatchList().add(match1);
        team3.getMatchList().add(match2);
        team4.getMatchList().add(match3);

        Match match6 = new Match(team1.getTeamName(), "PWSZ Tarnow", round1);

        //Creating Players of both teams
        Player Bryda = new Player(1, "Kat-Bry", "Katarzyna", "Bryda", team1);
        Player Colik = new Player(2, "Col-Kam", "Kamila", "Colik", team1);
        Player Szynkiel = new Player(3, "Zuz-Szy", "Zuzanna", "Szynkiel", team1);
        Player Stroiwas = new Player(5, "Pau-Str", "Paulina", "Stroiwas", team1);
        Player Lipska = new Player(7, "Ale-Lip", "Aleksandra", "Lipska", team1);
        Player Grabowska = new Player(8, "Mat-Gra", "Matylda", "Grabowska", team1);
        Player Lis = new Player(9, "Mal-Lis", "Malgorzata", "Lis", team1);
        Player Sciurka = new Player(10, "Dor-Sci", "Dorota", "Sciurka", team1);
        Player Czerwinska = new Player(12, "Agn-Cze", "Agnieszka", "Czerwinska", team1);
        Player Michalewicz = new Player(14, "Aga-Mic", "Agata", "Michalewicz", team1);
        Player Wilczek = new Player(15, "Ane-Wil", "Aneta", "Wilczek", team1);
        Player Szczygiol = new Player(16, "San-Szc", "Sandra", "Szczygiol", team1);
        Player Bodasinska = new Player(17, "Ann-Bod", "Anna", "Bodasinska", team1);

        Player Grodzka = new Player(1, "Maj-Gro", "Maja", "Grodzka", team2);
        Player Szabo = new Player(2, "Mag-Sza", "Magdalena", "Szabo", team2);
        Player Gliniecka = new Player(4, "Mar-Gli", "Marcelina", "Gliniecka", team2);
        Player Pytel = new Player(5, "Mag-Pyt", "Magdalena", "Pytel", team2);
        Player Glaz = new Player(6, "Pau-Gla", "Paulina", "Glaz", team2);
        Player Podlasek = new Player(7, "Sab-Pod", "Sabina", "Podlasek", team2);
        Player Jurczyk = new Player(8, "Mag-Jur", "Magdalena", "Jurczyk", team2);
        Player Sikorska = new Player(9, "Joa-Sik", "Joanna", "Sikorska", team2);
        Player Sobczak = new Player(12, "Adr-Sob", "Adrianna", "Sobczak", team2);
        Player Baldyga = new Player(13, "Pau-Bal", "Paulina", "Baldyga", team2);
        Player Mikolajewska = new Player(14, "Ale-Mik", "Aleksandra", "Mikolajewska", team2);
        Player Guzikiewicz = new Player(16, "Ale-Guz", "Aleksandra", "Guzikiewicz", team2);
        Player Mazurek = new Player(17, "Aga-Maz", "Agata", "Mazurek", team2);
        Player Grzelak = new Player(18, "Kla-Grz", "Klaudia", "Grzelak", team2);


        //Adding league to database
        leagueService.addLeague(FirstWoman);

        //Adding round to database
        roundService.addRound(round1);


        //Adding matches to database
        matchService.addMatch(match1);
        matchService.addMatch(match2);
        matchService.addMatch(match3);


        //Adding teams to database
        teamService.addTeam(team1);
        teamService.addTeam(team2);
        teamService.addTeam(team3);
        teamService.addTeam(team4);

        //Adding players to database
//        playerService.addPlayer(Bryda);
//        playerService.addPlayer(Colik);
//        playerService.addPlayer(Szynkiel);
//        playerService.addPlayer(Stroiwas);
//        playerService.addPlayer(Lipska);
//        playerService.addPlayer(Grabowska);
//        playerService.addPlayer(Lis);
//        playerService.addPlayer(Sciurka);
//        playerService.addPlayer(Czerwinska);
//        playerService.addPlayer(Michalewicz);
//        playerService.addPlayer(Wilczek);
//        playerService.addPlayer(Szczygiol);
//        playerService.addPlayer(Bodasinska);
//        playerService.addPlayer(Grodzka);
//        playerService.addPlayer(Szabo);
//        playerService.addPlayer(Gliniecka);
//        playerService.addPlayer(Pytel);
//        playerService.addPlayer(Glaz);
//        playerService.addPlayer(Podlasek);
//        playerService.addPlayer(Jurczyk);
//        playerService.addPlayer(Sikorska);
//        playerService.addPlayer(Sobczak);
//        playerService.addPlayer(Baldyga);
//        playerService.addPlayer(Mikolajewska);
//        playerService.addPlayer(Guzikiewicz);
//        playerService.addPlayer(Mazurek);
//        playerService.addPlayer(Grzelak);
    }
}
