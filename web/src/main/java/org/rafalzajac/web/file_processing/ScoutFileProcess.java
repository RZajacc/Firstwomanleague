package org.rafalzajac.web.file_processing;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rafalzajac.domain.*;
import org.rafalzajac.service.PlayerService;
import org.rafalzajac.service.PlayerStatsService;
import org.rafalzajac.service.TeamService;

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
//    private TeamStatsService teamStatsService;
    private AmazonClient amazonClient;

    // Data volley symbols describing events
    private static final String WINNING_SYMBOL = "#";
    private static final String POSITIVE_SYMBOL = "+";
    private static final String NEUTRAL_SYMBOL = "!";
    private static final String NEGATIVE_SYMBOL = "-";
    private static final String SLASH_SYMBOL = "/";
    private static final String LOSING_SYMBOL = "=";


    public ScoutFileProcess(String scoutFilePath, TeamService teamService, PlayerService playerService, PlayerStatsService playerStatsService, AmazonClient amazonClient) {
        this.scoutFilePath = scoutFilePath;
        this.teamService = teamService;
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
//        this.teamStatsService = teamStatsService;
        this.amazonClient = amazonClient;
    }


    public void processScoutFile() throws IOException {

        S3Object obj = amazonClient.getObjectFromServer(scoutFilePath);
        S3ObjectInputStream inputStream = obj.getObjectContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> scoutFileData = new ArrayList<>();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
            scoutFileData.add(inputLine);
        }


        scoutFileData.forEach(line -> {

            String teamInfoPattern = "(^[A-Z]{3,4})(;)([^;]{2,})(;)([0-9])(;)([^;]{0,})(;)([^;]{0,})(;)";
            String playerInfoPattern = "(^[0-9])(;)([0-9]?[0-9])(;)([0-9]?[0-9])(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([0-9*]{0,1})(;)([^;]{0,})(;)([^;]{0,})(;)([^;]{0,})(;)";
            String gamePattern = "([a*])([0-9]+[0-9])([A-Z])([A-Z])([#+!-=/])";

            Pattern gameEventsPattern = Pattern.compile(gamePattern);
            Pattern teamPattern = Pattern.compile(teamInfoPattern);
            Pattern playerPattern = Pattern.compile(playerInfoPattern);


            Matcher matcher1 = teamPattern.matcher(line);
            Matcher matcher2 = playerPattern.matcher(line);
            Matcher matcher3 = gameEventsPattern.matcher(line);

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

            //Sorting players by number
            homeTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
            awayTeam.getPlayerList().sort(Comparator.comparingInt(Player::getNumber));
    }


    public void createTeams() {

            // Data necessary for creating teams
            String[] data = teamData.get(0).split(";");
            String[] data1 = teamData.get(1).split(";");

            // Teams stats are needed later to populate with data from match.
//            TeamStats hTeamStats = new TeamStats();
            // Length is different depending if team has one or two coaches
            if (data.length == 3) {
                homeTeam = new Team(data[0], data[1], data[2]);
            } else {
                homeTeam = new Team(data[0], data[1], data[2], data[3]);
            }

//            TeamStats aTeamStats = new TeamStats();
            if (data1.length == 3) {
                awayTeam = new Team(data1[0], data1[1], data1[2]);
            } else {
                awayTeam = new Team(data1[0], data1[1], data1[2], data1[3]);
            }

        }


    public void createAllPlayers() {

        playerData.forEach(str-> {

            String [] teamInfoData = str.split(";");

            // Player stats are necessary for populating it later with data from scout file
            PlayerStats playerStats = new PlayerStats();

            // In scout file "0" means home team player and "1" describes away team player
            if(teamInfoData[0].equals("0")){
                createEachPlayer(homeTeam, teamInfoData, playerStats);
            }else {
                createEachPlayer(awayTeam, teamInfoData, playerStats);
            }
        });
    }


    public void createEachPlayer(Team team, String[] teamData, PlayerStats playerStats){
        Player playerToCreate = new Player(Integer.parseInt(teamData[1]), teamData[8], teamData[10], teamData[9], team, playerStats);
        playerToCreate.getPlayerStats().setStartingRotS1(teamData[3]);
        playerToCreate.getPlayerStats().setStartingRotS2(teamData[4]);
        playerToCreate.getPlayerStats().setStartingRotS3(teamData[5]);
        playerToCreate.getPlayerStats().setStartingRotS4(teamData[6]);
        playerToCreate.getPlayerStats().setStartingRotS5(teamData[7]);
        team.addPlayer(playerToCreate);
    }


    public void collectAllStats() {

        matchData.forEach(str -> {

            String[] eventData = str.split(";");

            // For events "*" means home team player, and "a" away team player
            if (eventData[0].equals("*")) {
                collectStatsForTeam(homeTeam, eventData);
            } else {
                collectStatsForTeam(awayTeam, eventData);
            }
        });
    }


    public void collectStatsForTeam (Team team, String[] eventData){

        for (Player playerToUpdate : team.getPlayerList()) {

            if (playerToUpdate.getNumber() == Integer.parseInt(eventData[1])) {

                // Method checks which event type is currently analyzed and based on that delegates processing to appropriate method
                evaluateScoutFileEvents(playerToUpdate, team, eventData);

                // Calculating player's points ratio(Won/lost)
                playerToUpdate.getPlayerStats().setPointsRatio(playerToUpdate.getPlayerStats().getPointsTotal() - playerToUpdate.getPlayerStats().getServeErrors() - playerToUpdate.getPlayerStats().getReceptionErrors() - playerToUpdate.getPlayerStats().getAttackErrors() - playerToUpdate.getPlayerStats().getAttackBlocked());

                // Same evaluation for whole team
                team.getTeamStats().setPointsRatio(team.getTeamStats().getPointsTotal() - team.getTeamStats().getServeErrors() - team.getTeamStats().getReceptionErrors() - team.getTeamStats().getAttackErrors() - team.getTeamStats().getAttackBlocked());

            }
        }
    }

    public void evaluateScoutFileEvents (Player playerToUpdate, Team teamtoUpdate, String[] eventData) {

        if (eventData[2].equals("S"))
            evaluateServeEvent(playerToUpdate, teamtoUpdate, eventData);
        if (eventData[2].equals("R"))
            evaluateReceptionEvent(playerToUpdate, teamtoUpdate, eventData);
        if (eventData[2].equals("A"))
            evaluateAttackEvent(playerToUpdate, teamtoUpdate, eventData);
        if (eventData[2].equals("B"))
            evaluateBlockEvent(playerToUpdate, teamtoUpdate, eventData);

    }

    public void evaluateServeEvent(Player playerToUpdate, Team teamToUpdate, String[] eventData) {

        String eventCategory = eventData[4];

        switch (eventCategory) {
            case WINNING_SYMBOL:
                playerToUpdate.getPlayerStats().setServeAce((playerToUpdate.getPlayerStats().getServeAce()) + 1);
                playerToUpdate.getPlayerStats().setServePositive((playerToUpdate.getPlayerStats().getServePositive()) + 1);
                playerToUpdate.getPlayerStats().setServeAttempts((playerToUpdate.getPlayerStats().getServeAttempts()) + 1);
                playerToUpdate.getPlayerStats().setPointsTotal((playerToUpdate.getPlayerStats().getPointsTotal()) + 1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setServeAce((teamToUpdate.getTeamStats().getServeAce()) + 1);
                teamToUpdate.getTeamStats().setServePositive((teamToUpdate.getTeamStats().getServePositive()) + 1);
                teamToUpdate.getTeamStats().setServeAttempts((teamToUpdate.getTeamStats().getServeAttempts()) + 1);
                teamToUpdate.getTeamStats().setPointsTotal((teamToUpdate.getTeamStats().getPointsTotal()) + 1);
                break;
            case POSITIVE_SYMBOL:
            case SLASH_SYMBOL:
                playerToUpdate.getPlayerStats().setServePositive((playerToUpdate.getPlayerStats().getServePositive()) + 1);
                playerToUpdate.getPlayerStats().setServeAttempts((playerToUpdate.getPlayerStats().getServeAttempts()) + 1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setServePositive((teamToUpdate.getTeamStats().getServePositive()) + 1);
                teamToUpdate.getTeamStats().setServeAttempts((teamToUpdate.getTeamStats().getServeAttempts()) + 1);
                break;
            case NEUTRAL_SYMBOL:
            case NEGATIVE_SYMBOL:
                playerToUpdate.getPlayerStats().setServeAttempts((playerToUpdate.getPlayerStats().getServeAttempts()) + 1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setServeAttempts((teamToUpdate.getTeamStats().getServeAttempts()) + 1);
                break;
            case LOSING_SYMBOL:
                playerToUpdate.getPlayerStats().setServeErrors((playerToUpdate.getPlayerStats().getServeErrors())+1);
                playerToUpdate.getPlayerStats().setServeAttempts((playerToUpdate.getPlayerStats().getServeAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setServeErrors((teamToUpdate.getTeamStats().getServeErrors())+1 );
                teamToUpdate.getTeamStats().setServeAttempts((teamToUpdate.getTeamStats().getServeAttempts())+1 );
                break;
            default:
                break;
        }

        if(playerToUpdate.getPlayerStats().getServeAttempts() != 0 ) {
            playerToUpdate.getPlayerStats().setServePositivePercent( (int)(( (float)(playerToUpdate.getPlayerStats().getServePositive())/(playerToUpdate.getPlayerStats().getServeAttempts())) * 100));

            // Same evaluation for whole team
            teamToUpdate.getTeamStats().setServePositivePercent( (int)(( (float)(teamToUpdate.getTeamStats().getServePositive())/(teamToUpdate.getTeamStats().getServeAttempts())) * 100));
        }
    }

    public void evaluateReceptionEvent(Player playerToUpdate, Team teamToUpdate, String[] eventData) {

        String eventCategory = eventData[4];

        switch (eventCategory) {
            case WINNING_SYMBOL:
                playerToUpdate.getPlayerStats().setReceptionPerfect((playerToUpdate.getPlayerStats().getReceptionPerfect())+1);
                playerToUpdate.getPlayerStats().setReceptionPositive((playerToUpdate.getPlayerStats().getReceptionPositive())+1);
                playerToUpdate.getPlayerStats().setReceptionAttempts((playerToUpdate.getPlayerStats().getReceptionAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setReceptionPerfect((teamToUpdate.getTeamStats().getReceptionPerfect())+1);
                teamToUpdate.getTeamStats().setReceptionPositive((teamToUpdate.getTeamStats().getReceptionPositive())+1);
                teamToUpdate.getTeamStats().setReceptionAttempts((teamToUpdate.getTeamStats().getReceptionAttempts())+1);
                break;
            case POSITIVE_SYMBOL:
                playerToUpdate.getPlayerStats().setReceptionPositive((playerToUpdate.getPlayerStats().getReceptionPositive())+1);
                playerToUpdate.getPlayerStats().setReceptionAttempts((playerToUpdate.getPlayerStats().getReceptionAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setReceptionPositive((teamToUpdate.getTeamStats().getReceptionPositive())+1);
                teamToUpdate.getTeamStats().setReceptionAttempts((teamToUpdate.getTeamStats().getReceptionAttempts())+1);
                break;
            case NEUTRAL_SYMBOL:
            case NEGATIVE_SYMBOL:
            case SLASH_SYMBOL:
                playerToUpdate.getPlayerStats().setReceptionAttempts((playerToUpdate.getPlayerStats().getReceptionAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setReceptionAttempts((teamToUpdate.getTeamStats().getReceptionAttempts())+1);
                break;
            case LOSING_SYMBOL:
                playerToUpdate.getPlayerStats().setReceptionErrors((playerToUpdate.getPlayerStats().getReceptionErrors())+1);
                playerToUpdate.getPlayerStats().setReceptionAttempts((playerToUpdate.getPlayerStats().getReceptionAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setReceptionErrors((teamToUpdate.getTeamStats().getReceptionErrors())+1);
                teamToUpdate.getTeamStats().setReceptionAttempts((teamToUpdate.getTeamStats().getReceptionAttempts())+1);
                break;
            default:
                break;
        }

        if(playerToUpdate.getPlayerStats().getReceptionAttempts() != 0 ) {
            playerToUpdate.getPlayerStats().setReceptionPositivePercent( (int)(( (float)( playerToUpdate.getPlayerStats().getReceptionPositive() )/(playerToUpdate.getPlayerStats().getReceptionAttempts())) * 100));
            playerToUpdate.getPlayerStats().setReceptionPerfectPercent( (int)(( (float)(playerToUpdate.getPlayerStats().getReceptionPerfect())/(playerToUpdate.getPlayerStats().getReceptionAttempts())) * 100));

            // Same evaluation for whole team
            teamToUpdate.getTeamStats().setReceptionPositivePercent((int)(( (float)( teamToUpdate.getTeamStats().getReceptionPositive() )/(teamToUpdate.getTeamStats().getReceptionAttempts())) * 100));
            teamToUpdate.getTeamStats().setReceptionPerfectPercent( (int)(( (float)(teamToUpdate.getTeamStats().getReceptionPerfect())/(teamToUpdate.getTeamStats().getReceptionAttempts())) * 100));

        }

    }

    public void evaluateAttackEvent(Player playerToUpdate, Team teamToUpdate, String[] eventData) {

        String eventCategory = eventData[4];

        switch (eventCategory) {
            case WINNING_SYMBOL:
                playerToUpdate.getPlayerStats().setAttackFinished((playerToUpdate.getPlayerStats().getAttackFinished())+1);
                playerToUpdate.getPlayerStats().setAttackAttempts((playerToUpdate.getPlayerStats().getAttackAttempts())+1);
                playerToUpdate.getPlayerStats().setPointsTotal((playerToUpdate.getPlayerStats().getPointsTotal())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setAttackFinished((teamToUpdate.getTeamStats().getAttackFinished())+1);
                teamToUpdate.getTeamStats().setAttackAttempts((teamToUpdate.getTeamStats().getAttackAttempts())+1);
                teamToUpdate.getTeamStats().setPointsTotal((teamToUpdate.getTeamStats().getPointsTotal())+1);
                break;
            case POSITIVE_SYMBOL:
            case NEUTRAL_SYMBOL:
            case NEGATIVE_SYMBOL:
                playerToUpdate.getPlayerStats().setAttackAttempts((playerToUpdate.getPlayerStats().getAttackAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setAttackAttempts((teamToUpdate.getTeamStats().getAttackAttempts())+1);
                break;
            case SLASH_SYMBOL:
                playerToUpdate.getPlayerStats().setAttackBlocked((playerToUpdate.getPlayerStats().getAttackBlocked())+1);
                playerToUpdate.getPlayerStats().setAttackAttempts((playerToUpdate.getPlayerStats().getAttackAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setAttackBlocked((teamToUpdate.getTeamStats().getAttackBlocked())+1);
                teamToUpdate.getTeamStats().setAttackAttempts((teamToUpdate.getTeamStats().getAttackAttempts())+1);
                break;
            case LOSING_SYMBOL:
                playerToUpdate.getPlayerStats().setAttackErrors((playerToUpdate.getPlayerStats().getAttackErrors())+1);
                playerToUpdate.getPlayerStats().setAttackAttempts((playerToUpdate.getPlayerStats().getAttackAttempts())+1);

                // Same evaluation for whole team
                teamToUpdate.getTeamStats().setAttackErrors((teamToUpdate.getTeamStats().getAttackErrors())+1);
                teamToUpdate.getTeamStats().setAttackAttempts((teamToUpdate.getTeamStats().getAttackAttempts())+1);
                break;
            default:
                break;
        }

        if(playerToUpdate.getPlayerStats().getAttackAttempts() != 0 ) {
            playerToUpdate.getPlayerStats().setAttackFinishedPercent((int)(( (float)( playerToUpdate.getPlayerStats().getAttackFinished() )/(playerToUpdate.getPlayerStats().getAttackAttempts())) * 100));

            // Same evaluation for whole team
            teamToUpdate.getTeamStats().setAttackFinishedPercent((int)(( (float)( teamToUpdate.getTeamStats().getAttackFinished() )/(teamToUpdate.getTeamStats().getAttackAttempts())) * 100));

        }
    }

    public void evaluateBlockEvent(Player playerToUpdate, Team teamToUpdate, String[] eventData) {


        if (eventData[4].equals(WINNING_SYMBOL)) {
            playerToUpdate.getPlayerStats().setBlockScore((playerToUpdate.getPlayerStats().getBlockScore()) + 1);
            playerToUpdate.getPlayerStats().setPointsTotal((playerToUpdate.getPlayerStats().getPointsTotal()) + 1);

            // Same evaluation for whole team
            teamToUpdate.getTeamStats().setBlockScore((teamToUpdate.getTeamStats().getBlockScore()) + 1);
            teamToUpdate.getTeamStats().setPointsTotal((teamToUpdate.getTeamStats().getPointsTotal()) + 1);
        }

    }



    public void saveStatsToDatabase() {

        Team t1 = teamService.getTeamByTag(homeTeam.getTeamTag());
        Team t2 = teamService.getTeamByTag(awayTeam.getTeamTag());

            // Looping through players created from scout file and comparing with list in data base. If player numbers match
            // then their data is updated
            t1.getPlayerList().forEach(playerToUpdate -> {
                for (Player player1 : homeTeam.getPlayerList()) {
                    if (playerToUpdate.getNumber() == player1.getNumber()){

                        PlayerStats stats = playerToUpdate.getPlayerStats();
                        stats.setPointsTotal(stats.getPointsTotal() + player1.getPlayerStats().getPointsTotal());
                        stats.setServeAce(stats.getServeAce() + player1.getPlayerStats().getServeAce());
                        stats.setAttackAttempts(stats.getAttackAttempts() + player1.getPlayerStats().getAttackAttempts());
                        stats.setAttackFinished(stats.getAttackFinished() + player1.getPlayerStats().getAttackFinished());
                        stats.setAttackFinishedPercent((int)(( (float)( stats.getAttackFinished() )/(stats.getAttackAttempts())) * 100));

                        stats.setReceptionPositive(stats.getReceptionPositive() + player1.getPlayerStats().getReceptionPositive());
                        stats.setReceptionPerfect(stats.getReceptionPerfect() + player1.getPlayerStats().getReceptionPerfect());
                        stats.setReceptionAttempts(stats.getReceptionAttempts() + player1.getPlayerStats().getReceptionAttempts());

                        stats.setReceptionPositivePercent( (int)(( (float)( stats.getReceptionPositive() )/(stats.getReceptionAttempts())) * 100));
                        stats.setReceptionPerfectPercent( (int)(( (float)( stats.getReceptionPerfect() )/(stats.getReceptionAttempts())) * 100));

                        stats.setBlockScore(stats.getBlockScore() + player1.getPlayerStats().getBlockScore());

                        playerStatsService.savePlayerStats(stats);
                        playerToUpdate.setPlayerStats(stats);
                        playerService.addPlayer(playerToUpdate);
                    }
                }
            });

            // Looping through players created from scout file and comparing with list in data base. If player numbers match
            // then their data is updated
            t2.getPlayerList().forEach(playerToUpdate -> {
                for (Player player1 : awayTeam.getPlayerList()) {
                    if (playerToUpdate.getNumber() == player1.getNumber()){

                        PlayerStats stats = playerToUpdate.getPlayerStats();
                        stats.setPointsTotal(stats.getPointsTotal() + player1.getPlayerStats().getPointsTotal());
                        stats.setServeAce(stats.getServeAce() + player1.getPlayerStats().getServeAce());
                        stats.setAttackAttempts(stats.getAttackAttempts() + player1.getPlayerStats().getAttackAttempts());
                        stats.setAttackFinished(stats.getAttackFinished() + player1.getPlayerStats().getAttackFinished());
                        stats.setAttackFinishedPercent((int)(( (float)( stats.getAttackFinished() )/(stats.getAttackAttempts())) * 100));

                        stats.setReceptionPositive(stats.getReceptionPositive() + player1.getPlayerStats().getReceptionPositive());
                        stats.setReceptionPerfect(stats.getReceptionPerfect() + player1.getPlayerStats().getReceptionPerfect());
                        stats.setReceptionAttempts(stats.getReceptionAttempts() + player1.getPlayerStats().getReceptionAttempts());

                        stats.setReceptionPositivePercent( (int)(( (float)( stats.getReceptionPositive() )/(stats.getReceptionAttempts())) * 100));
                        stats.setReceptionPerfectPercent( (int)(( (float)( stats.getReceptionPerfect() )/(stats.getReceptionAttempts())) * 100));

                        stats.setBlockScore(stats.getBlockScore() + player1.getPlayerStats().getBlockScore());

                        playerStatsService.savePlayerStats(stats);
                        playerToUpdate.setPlayerStats(stats);
                        playerService.addPlayer(playerToUpdate);
                    }
                }
            });

            teamService.addTeam(t1);
        }
    }
