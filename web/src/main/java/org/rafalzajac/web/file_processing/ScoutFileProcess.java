package org.rafalzajac.web.file_processing;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.PlayerService;
import org.rafalzajac.service.PlayerStatsService;
import org.rafalzajac.service.TeamService;
import org.rafalzajac.service.TeamStatsService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class ScoutFileProcess {

    private String scoutFilePath;
    private Team homeTeam;
    private Team awayTeam;
    private Player player;
    private List<String> teamData = new LinkedList<>();
    private List<String> playerData = new LinkedList<>();
    private List<String> matchData = new LinkedList<>();
    private PlayerService playerService;
    private TeamService teamService;
    private PlayerStatsService playerStatsService;
    private TeamStatsService teamStatsService;
    private AmazonClient amazonClient;

    public ScoutFileProcess(String scoutFilePath, TeamService teamService, PlayerService playerService, PlayerStatsService playerStatsService, TeamStatsService teamStatsService, AmazonClient amazonClient) {
        this.scoutFilePath = scoutFilePath;
        this.teamService = teamService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.teamStatsService = teamStatsService;
        this.amazonClient = amazonClient;
    }

    public void processScoutFile() throws IOException {

        S3Object obj = amazonClient.getObjectFromServer(scoutFilePath);
        S3ObjectInputStream inputStream = obj.getObjectContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = new ArrayList<>();
        String inputLine = null;

        while ((inputLine = br.readLine()) != null) {
            lines.add(inputLine);
        }

   // List<String> lines = Files.readAllLines(inputStream, Charset.forName("ISO-8859-1"));

        lines.forEach((line) -> {

            String teamPattern = "(^[A-Z]{3,4})(;)([^;]{2,})(;)([0-9])(;)([^;]{0,})(;)([^;]{0,})(;)";
            String playerPattern = "(^[0-9])(;)([0-9]?[0-9])(;)([0-9]?[0-9])(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([^;]{0,})(;)([^;]{0,})(;)([^;]{0,})(;)";
            String gameEventsPattern = "([a*])([0-9]+[0-9])([A-Z])([A-Z])([#+!-=/])";

            Pattern statPattern = Pattern.compile(gameEventsPattern);
            Pattern team = Pattern.compile(teamPattern);
            Pattern player = Pattern.compile(playerPattern);


            Matcher matcher1 = team.matcher(line);
            Matcher matcher2 = player.matcher(line);
            Matcher matcher3 = statPattern.matcher(line);

            if(matcher1.find()){
                teamData.add(matcher1.group(1) + matcher1.group(2) + matcher1.group(3) + matcher1.group(4)
                        + matcher1.group(7) +matcher1.group(8) + matcher1.group(9));
            }

            if(matcher2.find()){
                playerData.add(matcher2.group(1) + matcher2.group(2) + matcher2.group(3) + matcher2.group(4)
                        + matcher2.group(5) + matcher2.group(6) + matcher2.group(7) + matcher2.group(8)
                        + matcher2.group(9) + matcher2.group(10) + matcher2.group(11) + matcher2.group(12)
                        + matcher2.group(13) + matcher2.group(14) + matcher2.group(15) + matcher2.group(16)
                        + matcher2.group(17) + matcher2.group(18) + matcher2.group(19) + matcher2.group(20)
                        + matcher2.group(21) + matcher2.group(22));
            }

            if(matcher3.find()) {
                matchData.add(matcher3.group(1) + ";" + matcher3.group(2) + ";"+ matcher3.group(3) + ";"+ matcher3.group(4) + ";" + matcher3.group(5));
            }

        });

            createTeams();
            createAllPlayers();
            collectAllStats();

            //Sorting players by number - sometimes its messed up
            homeTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
            awayTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
    }


    public void createTeams() {

            // Zespoły tworzę ręcznie bo zawsze są dwa i informacje sa zawsze w tym samym miejscu
            String[] data = teamData.get(0).split(";");
            String[] data1 = teamData.get(1).split(";");

        System.out.println("data length" + data.length);
        System.out.println("data length" + data1.length);

            TeamStats hTeamStats = new TeamStats();
            if (data.length == 3) {
                homeTeam = new Team(data[0], data[1], data[2], hTeamStats);
            } else {
                homeTeam = new Team(data[0], data[1], data[2], data[3], hTeamStats);
            }

            TeamStats aTeamStats = new TeamStats();
            if (data1.length == 3) {
                awayTeam = new Team(data1[0], data1[1], data1[2], aTeamStats);
            } else {
                awayTeam = new Team(data1[0], data1[1], data1[2], data1[3], aTeamStats);
            }

        }


    public void createAllPlayers() {

        playerData.forEach((str)-> {

            String [] teamData = str.split(";");
            PlayerStats playerStats = new PlayerStats();

            // W przypadk zawodnikóa "0" identyfikuje zespół gospodarza, "1" gościa
            if(teamData[0].equals("0")){
                createEachPlayer(homeTeam, teamData, playerStats);
            }else {
                createEachPlayer(awayTeam, teamData, playerStats);
            }
        });
    }


    public void createEachPlayer(Team team, String[] teamData, PlayerStats playerStats){
        Player player = new Player(Integer.parseInt(teamData[1]), teamData[8], teamData[10], teamData[9], team, playerStats);
        player.getPlayerStats().setStartingRotS1(teamData[3]);
        player.getPlayerStats().setStartingRotS2(teamData[4]);
        player.getPlayerStats().setStartingRotS3(teamData[5]);
        player.getPlayerStats().setStartingRotS4(teamData[6]);
        player.getPlayerStats().setStartingRotS5(teamData[7]);
        team.addPlayer(player);
    }


    public void collectAllStats() {

        matchData.forEach((str) -> {

            String[] eventData = str.split(";");

            // "*" w pliku scouta oznacza drużynę gospodarza, "a" gościa
            if (eventData[0].equals("*")) {
                collectStatsForTeam(homeTeam, eventData);
            } else {
                collectStatsForTeam(awayTeam, eventData);
            }
        });
    }


    public void collectStatsForTeam (Team team, String[] eventData){

        for (Player player : team.getPlayerList()) {

            if (player.getNumber() == Integer.parseInt(eventData[1])) {

                // Parametr pierwsze "S" - Serve oznacza zagrywkę, drugi ocenia akcję jakościowo i ma wartość[#,+,!,/,-,=]
                // "#" - jest zagraniem punktowym(poza przyjęciem), "=" - zawsze błędnym, pozostałe zależą od tego czego dotyczą
                if (eventData[2].equals("S") && eventData[4].equals("#")){
                    player.getPlayerStats().setServeAce((player.getPlayerStats().getServeAce())+1);
                    player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                    player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServeAce((team.getTeamStats().getServeAce())+1 );
                    team.getTeamStats().setServePositive((team.getTeamStats().getServePositive())+1 );
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );
                    team.getTeamStats().setPointsTotal((team.getTeamStats().getPointsTotal())+1 );

                } else if (eventData[2].equals("S") && eventData[4].equals("+")){
                    player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServePositive((team.getTeamStats().getServePositive())+1 );
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );

                } else if (eventData[2].equals("S") && eventData[4].equals("!")){
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );

                } else if (eventData[2].equals("S") && eventData[4].equals("-")){
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );

                } else if (eventData[2].equals("S") && eventData[4].equals("/")){
                    player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServePositive((team.getTeamStats().getServePositive())+1 );
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );

                } else if (eventData[2].equals("S") && eventData[4].equals("=")){
                    player.getPlayerStats().setServeErrors((player.getPlayerStats().getServeErrors())+1);
                    player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServeErrors((team.getTeamStats().getServeErrors())+1 );
                    team.getTeamStats().setServeAttempts((team.getTeamStats().getServeAttempts())+1 );
                }

                // Parametr pierwsze "R" - Reception oznacza przyjęcie, drugi ocenia akcję jakościowo i ma wartość[#,+,!,/,-,=]
                if (eventData[2].equals("R") && eventData[4].equals("#")){
                    player.getPlayerStats().setReceptionPerfect((player.getPlayerStats().getReceptionPerfect())+1);
                    player.getPlayerStats().setReceptionPositive((player.getPlayerStats().getReceptionPositive())+1);
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionPerfect((team.getTeamStats().getReceptionPerfect())+1);
                    team.getTeamStats().setReceptionPositive((team.getTeamStats().getReceptionPositive())+1);
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);

                } else if (eventData[2].equals("R") && eventData[4].equals("+")){
                    player.getPlayerStats().setReceptionPositive((player.getPlayerStats().getReceptionPositive())+1);
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionPositive((team.getTeamStats().getReceptionPositive())+1);
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);

                } else if (eventData[2].equals("R") && eventData[4].equals("!")){
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);

                } else if (eventData[2].equals("R") && eventData[4].equals("-")){
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);

                } else if (eventData[2].equals("R") && eventData[4].equals("/")){
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);

                } else if (eventData[2].equals("R") && eventData[4].equals("=")){
                    player.getPlayerStats().setReceptionErrors((player.getPlayerStats().getReceptionErrors())+1);
                    player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionErrors((team.getTeamStats().getReceptionErrors())+1);
                    team.getTeamStats().setReceptionAttempts((team.getTeamStats().getReceptionAttempts())+1);
                }

                // Parametr pierwsze "A" - Attack oznacza atak, drugi ocenia akcję jakościowo i ma wartość[#,+,!,/,-,=]
                if (eventData[2].equals("A") && eventData[4].equals("#")){
                    player.getPlayerStats().setAttackFinished((player.getPlayerStats().getAttackFinished())+1);
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);
                    player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackFinished((team.getTeamStats().getAttackFinished())+1);
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);
                    team.getTeamStats().setPointsTotal((team.getTeamStats().getPointsTotal())+1);

                } else if (eventData[2].equals("A") && eventData[4].equals("+")){
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);

                } else if (eventData[2].equals("A") && eventData[4].equals("!")){
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);

                } else if (eventData[2].equals("A") && eventData[4].equals("-")){
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);

                } else if (eventData[2].equals("A") && eventData[4].equals("/")){
                    player.getPlayerStats().setAttackBlocked((player.getPlayerStats().getAttackBlocked())+1);
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackBlocked((team.getTeamStats().getAttackBlocked())+1);
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);

                } else if (eventData[2].equals("A") && eventData[4].equals("=")){
                    player.getPlayerStats().setAttackErrors((player.getPlayerStats().getAttackErrors())+1);
                    player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackErrors((team.getTeamStats().getAttackErrors())+1);
                    team.getTeamStats().setAttackAttempts((team.getTeamStats().getAttackAttempts())+1);
                }

                // Parametr pierwsze "B" - Block oznacza blok
                if (eventData[2].equals("B") && eventData[4].equals("#")) {
                    player.getPlayerStats().setBlockScore((player.getPlayerStats().getBlockScore()) + 1);
                    player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal()) + 1);

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setBlockScore((team.getTeamStats().getBlockScore()) + 1);
                    team.getTeamStats().setPointsTotal((team.getTeamStats().getPointsTotal()) + 1);
                }

                // Obliczanie procentowej skutecznosci zagrywki
                if(player.getPlayerStats().getServeAttempts() != 0 ) {
                    player.getPlayerStats().setServePositivePercent(Math.round( (int)(( (float)(player.getPlayerStats().getServePositive())/(player.getPlayerStats().getServeAttempts())) * 100)));

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setServePositivePercent(Math.round( (int)(( (float)(team.getTeamStats().getServePositive())/(team.getTeamStats().getServeAttempts())) * 100)));
                }
                // Obliczanie procentowej skutecznosci przyjęcia
                if(player.getPlayerStats().getReceptionAttempts() != 0 ) {
                    player.getPlayerStats().setReceptionPositivePercent(Math.round( (int)(( (float)( player.getPlayerStats().getReceptionPositive() )/(player.getPlayerStats().getReceptionAttempts())) * 100)));
                    player.getPlayerStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(player.getPlayerStats().getReceptionPerfect())/(player.getPlayerStats().getReceptionAttempts())) * 100)));

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setReceptionPositivePercent(Math.round( (int)(( (float)( team.getTeamStats().getReceptionPositive() )/(team.getTeamStats().getReceptionAttempts())) * 100)));
                    team.getTeamStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(team.getTeamStats().getReceptionPerfect())/(team.getTeamStats().getReceptionAttempts())) * 100)));

                }
                // Obliczanie procentowej skutecznosci ataku
                if(player.getPlayerStats().getAttackAttempts() != 0 ) {
                    player.getPlayerStats().setAttackFinishedPercent(Math.round( (int)(( (float)( player.getPlayerStats().getAttackFinished() )/(player.getPlayerStats().getAttackAttempts())) * 100)));

                    // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                    team.getTeamStats().setAttackFinishedPercent(Math.round( (int)(( (float)( team.getTeamStats().getAttackFinished() )/(team.getTeamStats().getAttackAttempts())) * 100)));

                }

                // Obliczanie ratio punktów (Zdobyte - stracone)
                player.getPlayerStats().setPointsRatio(player.getPlayerStats().getPointsTotal() - player.getPlayerStats().getServeErrors() - player.getPlayerStats().getReceptionErrors() - player.getPlayerStats().getAttackErrors() - player.getPlayerStats().getAttackBlocked());

                // To samo robię do statystyk drużyny aby potem wyświetlić łączną wartość
                team.getTeamStats().setPointsRatio(team.getTeamStats().getPointsTotal() - team.getTeamStats().getServeErrors() - team.getTeamStats().getReceptionErrors() - team.getTeamStats().getAttackErrors() - team.getTeamStats().getAttackBlocked());

            }
        }
    }

    public void saveStatsToDatabase() {

        Team t1 = teamService.getTeamByTag(homeTeam.getTeamTag());
        Team t2 = teamService.getTeamByTag(awayTeam.getTeamTag());



        if (t1 != null) {
            t1.getPlayerList().forEach((player) -> {
                for (Player player1 : homeTeam.getPlayerList()) {
                    if (player.getNumber() == player1.getNumber()){

                        PlayerStats stats = player.getPlayerStats();
                        stats.setPointsTotal(stats.getPointsTotal() + player1.getPlayerStats().getPointsTotal());
                        stats.setServeAce(stats.getServeAce() + player1.getPlayerStats().getServeAce());
                        stats.setAttackAttempts(stats.getAttackAttempts() + player1.getPlayerStats().getAttackAttempts());
                        stats.setAttackFinished(stats.getAttackFinished() + player1.getPlayerStats().getAttackFinished());
                        stats.setAttackFinishedPercent(Math.round( (int)(( (float)( stats.getAttackFinished() )/(stats.getAttackAttempts())) * 100)));

                        stats.setReceptionPositive(stats.getReceptionPositive() + player1.getPlayerStats().getReceptionPositive());
                        stats.setReceptionPerfect(stats.getReceptionPerfect() + player1.getPlayerStats().getReceptionPerfect());
                        stats.setReceptionAttempts(stats.getReceptionAttempts() + player1.getPlayerStats().getReceptionAttempts());

                        stats.setReceptionPositivePercent(Math.round( (int)(( (float)( stats.getReceptionPositive() )/(stats.getReceptionAttempts())) * 100)));
                        stats.setReceptionPerfectPercent(Math.round( (int)(( (float)( stats.getReceptionPerfect() )/(stats.getReceptionAttempts())) * 100)));

                        stats.setBlockScore(stats.getBlockScore() + player1.getPlayerStats().getBlockScore());

                        playerStatsService.savePlayerStats(stats);
                        player.setPlayerStats(stats);
                        playerService.addPlayer(player);
                    }
                }
            });
        } else {
            TeamStats teamStats = homeTeam.getTeamStats();
            teamStatsService.saveTeamStats(teamStats);
            homeTeam.setTeamStats(teamStats);
            teamService.addTeam(homeTeam);

            homeTeam.getPlayerList().forEach((player) -> {

            PlayerStats stats = player.getPlayerStats();
            playerStatsService.savePlayerStats(stats);
            player.setPlayerStats(stats);
            playerService.addPlayer(player);
        });

        }

        if (t2 != null) {
            t2.getPlayerList().forEach((player) -> {
                for (Player player1 : awayTeam.getPlayerList()) {
                    if (player.getNumber() == player1.getNumber()){

                        PlayerStats stats = player.getPlayerStats();
                        stats.setPointsTotal(stats.getPointsTotal() + player1.getPlayerStats().getPointsTotal());
                        stats.setServeAce(stats.getServeAce() + player1.getPlayerStats().getServeAce());
                        stats.setAttackAttempts(stats.getAttackAttempts() + player1.getPlayerStats().getAttackAttempts());
                        stats.setAttackFinished(stats.getAttackFinished() + player1.getPlayerStats().getAttackFinished());
                        stats.setAttackFinishedPercent(Math.round( (int)(( (float)( stats.getAttackFinished() )/(stats.getAttackAttempts())) * 100)));

                        stats.setReceptionPositive(stats.getReceptionPositive() + player1.getPlayerStats().getReceptionPositive());
                        stats.setReceptionPerfect(stats.getReceptionPerfect() + player1.getPlayerStats().getReceptionPerfect());
                        stats.setReceptionAttempts(stats.getReceptionAttempts() + player1.getPlayerStats().getReceptionAttempts());

                        stats.setReceptionPositivePercent(Math.round( (int)(( (float)( stats.getReceptionPositive() )/(stats.getReceptionAttempts())) * 100)));
                        stats.setReceptionPerfectPercent(Math.round( (int)(( (float)( stats.getReceptionPerfect() )/(stats.getReceptionAttempts())) * 100)));

                        stats.setBlockScore(stats.getBlockScore() + player1.getPlayerStats().getBlockScore());

                        playerStatsService.savePlayerStats(stats);
                        player.setPlayerStats(stats);
                        playerService.addPlayer(player);
                    }
                }
            });
        } else {
            TeamStats teamStats2 = awayTeam.getTeamStats();
            teamStatsService.saveTeamStats(teamStats2);
            awayTeam.setTeamStats(teamStats2);
            teamService.addTeam(awayTeam);

            awayTeam.getPlayerList().forEach((player) -> {

            PlayerStats stats = player.getPlayerStats();
            playerStatsService.savePlayerStats(stats);
            player.setPlayerStats(stats);
            playerService.addPlayer(player);
        });
        }



    }


}