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
    private MatchResultService matchResultService;
    private TeamService teamService;
    private PlayerService playerService;
    private PlayerStatsService playerStatsService;
    private TeamStatsService teamStatsService;

    public DataBaseLoader(LeagueService leagueService, RoundService roundService, MatchService matchService, TeamService teamService, PlayerService playerService, PlayerStatsService playerStatsService, TeamStatsService teamStatsService, MatchResultService matchResultService) {
        this.leagueService = leagueService;
        this.roundService = roundService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.teamStatsService = teamStatsService;
        this.matchResultService = matchResultService;
    }



    @Override
    public void run(String... args) throws Exception {

        //Creating league instance
        League FirstWoman = new League("1LK", "2018-2019");

        //Creating round instance
        Round round1 = new Round(1, FirstWoman);

        MatchResult result1 = new MatchResult();
        MatchResult result2 = new MatchResult();
        MatchResult result3 = new MatchResult();

        //Creating match instances
        Match match1 = new Match("MKS Dabrowa", "PWSZ Tarnow", 1, round1, result1);
        Match match2 = new Match("Joker Swiecie", "MKS Dabrowa",2,  round1, result2);
        Match match3 = new Match("MKS Dabrowa", "Energetyk Poznan", 3, round1, result3);



        TeamStats team1Stats = new TeamStats();
        TeamStats team2Stats = new TeamStats();
        TeamStats team3Stats = new TeamStats();
        TeamStats team4Stats = new TeamStats();

        //Creating team instances
        Team team1 = new Team("MKS", "MKS Dąbrowa Górnicza", "Adam Grabowsk", "Ireneusz Borzęcki", team1Stats);
        Team team2 = new Team("PWSZ", "PWSZ Tarnow", "Michal Betleja", "Michal Madejski", team2Stats);
        Team team3 = new Team("JOK", "Joker Swiecie", "Michal Wojtowicz", "Milosz Szwaba", team3Stats);
        Team team4 = new Team("ENE", "Energetyk Poznan", "Michal Patyk", team4Stats);

        team1.getMatchList().add(match1);
        team1.getMatchList().add(match2);
        team1.getMatchList().add(match3);

        team2.getMatchList().add(match1);
        team3.getMatchList().add(match2);
        team4.getMatchList().add(match3);

        PlayerStats playerStats1 = new PlayerStats();
        PlayerStats playerStats2 = new PlayerStats();
        PlayerStats playerStats3 = new PlayerStats();
        PlayerStats playerStats4 = new PlayerStats();

        //Creating Players of both teams
        Player Bryda = new Player(1, "Kat-Bry", "Katarzyna", "Bryda", team1, playerStats1);
        Player Colik = new Player(2, "Col-Kam", "Kamila", "Colik", team1, playerStats2);
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

        Player Grodzka = new Player(1, "Maj-Gro", "Maja", "Grodzka", team2, playerStats3);
        Player Szabo = new Player(2, "Mag-Sza", "Magdalena", "Szabo", team2, playerStats4);
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

        //Adding match result
        matchResultService.saveMatchResult(result1);
        matchResultService.saveMatchResult(result2);
        matchResultService.saveMatchResult(result3);

        //Adding matches to database
        matchService.addMatch(match1);
        matchService.addMatch(match2);
        matchService.addMatch(match3);

        //Adding team stats to database
        teamStatsService.saveTeamStats(team1Stats);
        teamStatsService.saveTeamStats(team2Stats);
        teamStatsService.saveTeamStats(team3Stats);
        teamStatsService.saveTeamStats(team4Stats);

        //Adding teams to database
        teamService.addTeam(team1);
        teamService.addTeam(team2);
        teamService.addTeam(team3);
        teamService.addTeam(team4);

        playerStatsService.savePlayerStats(playerStats1);
        playerStatsService.savePlayerStats(playerStats2);
        playerStatsService.savePlayerStats(playerStats3);
        playerStatsService.savePlayerStats(playerStats4);


        //Adding players to database
        playerService.addPlayer(Bryda);
        playerService.addPlayer(Colik);
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
        playerService.addPlayer(Grodzka);
        playerService.addPlayer(Szabo);
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
