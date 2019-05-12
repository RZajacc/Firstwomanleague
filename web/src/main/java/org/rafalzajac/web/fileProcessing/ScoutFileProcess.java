package org.rafalzajac.web.fileProcessing;


import lombok.Data;
import org.rafalzajac.domain.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ScoutFileProcess {

    private Path scoutFilePath;
    private Team homeTeam;
    private Team awayTeam;
    private Player player;
    List<String> teamData = new LinkedList<>();
    List<String> playerData = new LinkedList<>();
    List<String> matchData = new LinkedList<>();

    public ScoutFileProcess(Path scoutFilePath) {
        this.scoutFilePath = scoutFilePath;
    }

    public void processScoutFile() throws Exception {

    List<String> lines = Files.readAllLines(scoutFilePath, Charset.forName("ISO-8859-1"));


        for (String line : lines) {
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
           }

            createTeams();
            createPlayers();
            collectStats();
          }

    public void createTeams() {
            String[] data = teamData.get(0).split(";");
            TeamStats hTeamStats = new TeamStats();
            homeTeam = new Team(data[0], data[1], data[2], data[3], hTeamStats);
            TeamStats aTeamStats = new TeamStats();
            String[] data1 = teamData.get(1).split(";");
            awayTeam = new Team(data1[0], data1[1], data1[2], data1[3], aTeamStats);
        }


    public void createPlayers() {
        for (String str : playerData) {

            String [] homeTeamPlayersData = str.split(";");
            PlayerStats playerStats = new PlayerStats();

            if(homeTeamPlayersData[0].equals("0")) {

                Player player = new Player(Integer.parseInt(homeTeamPlayersData[1]), homeTeamPlayersData[8], homeTeamPlayersData[9], homeTeamPlayersData[10], homeTeam, playerStats);
                player.getPlayerStats().setStartingRotS1(homeTeamPlayersData[3]);
                player.getPlayerStats().setStartingRotS2(homeTeamPlayersData[4]);
                player.getPlayerStats().setStartingRotS3(homeTeamPlayersData[5]);
                player.getPlayerStats().setStartingRotS4(homeTeamPlayersData[6]);
                player.getPlayerStats().setStartingRotS5(homeTeamPlayersData[7]);
                homeTeam.addPlayer(player);
            }

            if(homeTeamPlayersData[0].equals("1")) {

                Player player = new Player(Integer.parseInt(homeTeamPlayersData[1]), homeTeamPlayersData[8], homeTeamPlayersData[9], homeTeamPlayersData[10], awayTeam, playerStats);
                player.getPlayerStats().setStartingRotS1(homeTeamPlayersData[3]);
                player.getPlayerStats().setStartingRotS2(homeTeamPlayersData[4]);
                player.getPlayerStats().setStartingRotS3(homeTeamPlayersData[5]);
                player.getPlayerStats().setStartingRotS4(homeTeamPlayersData[6]);
                player.getPlayerStats().setStartingRotS5(homeTeamPlayersData[7]);
                awayTeam.addPlayer(player);
            }
        }

    }

    public void collectStats() {
        for (String str: matchData) {

            String [] data = str.split(";");

            //Statystyki drużyny gospodarza
            if (data[0].equals("*")){

                for (Player player : homeTeam.getPlayerList()) {

                    if(player.getNumber() == Integer.parseInt(data[1])){

                        // Parametry zagrywki
                        if (data[2].equals("S") && data[4].equals("#")){
                            player.getPlayerStats().setServeAce((player.getPlayerStats().getServeAce())+1);
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                            player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServeAce((homeTeam.getTeamStats().getServeAce())+1 );
                            homeTeam.getTeamStats().setServePositive((homeTeam.getTeamStats().getServePositive())+1 );
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );
                            homeTeam.getTeamStats().setPointsTotal((homeTeam.getTeamStats().getPointsTotal())+1 );

                        } else if (data[2].equals("S") && data[4].equals("+")){
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServePositive((homeTeam.getTeamStats().getServePositive())+1 );
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("!")){
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("-")){
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("/")){
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServePositive((homeTeam.getTeamStats().getServePositive())+1 );
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("=")){
                            player.getPlayerStats().setServeErrors((player.getPlayerStats().getServeErrors())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setServeErrors((homeTeam.getTeamStats().getServeErrors())+1 );
                            homeTeam.getTeamStats().setServeAttempts((homeTeam.getTeamStats().getServeAttempts())+1 );
                        }

                        // Parametry przyjęcia
                        if (data[2].equals("R") && data[4].equals("#")){
                            player.getPlayerStats().setReceptionPerfect((player.getPlayerStats().getReceptionPerfect())+1);
                            player.getPlayerStats().setReceptionPositive((player.getPlayerStats().getReceptionPositive())+1);
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionPerfect((homeTeam.getTeamStats().getReceptionPerfect())+1);
                            homeTeam.getTeamStats().setReceptionPositive((homeTeam.getTeamStats().getReceptionPositive())+1);
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);


                        } else if (data[2].equals("R") && data[4].equals("+")){
                            player.getPlayerStats().setReceptionPositive((player.getPlayerStats().getReceptionPositive())+1);
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionPositive((homeTeam.getTeamStats().getReceptionPositive())+1);
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("!")){
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("-")){
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("/")){
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("=")){
                            player.getPlayerStats().setReceptionErrors((player.getPlayerStats().getReceptionErrors())+1);
                            player.getPlayerStats().setReceptionAttempts((player.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setReceptionErrors((homeTeam.getTeamStats().getReceptionErrors())+1);
                            homeTeam.getTeamStats().setReceptionAttempts((homeTeam.getTeamStats().getReceptionAttempts())+1);
                        }

                        // Parametry ataku
                        if (data[2].equals("A") && data[4].equals("#")){
                            player.getPlayerStats().setAttackFinished((player.getPlayerStats().getAttackFinished())+1);
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);
                            player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackFinished((homeTeam.getTeamStats().getAttackFinished())+1);
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);
                            homeTeam.getTeamStats().setPointsTotal((homeTeam.getTeamStats().getPointsTotal())+1);


                        } else if (data[2].equals("A") && data[4].equals("+")){
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("!")){
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("-")){
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("/")){
                            player.getPlayerStats().setAttackBlocked((player.getPlayerStats().getAttackBlocked())+1);
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackBlocked((homeTeam.getTeamStats().getAttackBlocked())+1);
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("=")){
                            player.getPlayerStats().setAttackErrors((player.getPlayerStats().getAttackErrors())+1);
                            player.getPlayerStats().setAttackAttempts((player.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setAttackErrors((homeTeam.getTeamStats().getAttackErrors())+1);
                            homeTeam.getTeamStats().setAttackAttempts((homeTeam.getTeamStats().getAttackAttempts())+1);
                        }

                        // Parametry bloku
                        if (data[2].equals("B") && data[4].equals("#")) {
                            player.getPlayerStats().setBlockScore((player.getPlayerStats().getBlockScore()) + 1);
                            player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal()) + 1);

                            // To samo do podsumowania dla drużyny
                            homeTeam.getTeamStats().setBlockScore((homeTeam.getTeamStats().getBlockScore()) + 1);
                            homeTeam.getTeamStats().setPointsTotal((homeTeam.getTeamStats().getPointsTotal()) + 1);
                        }

                        // Obliczanie procentowej skutecznosci zagrywki
                        if(player.getPlayerStats().getServeAttempts() != 0 ) {
                            player.getPlayerStats().setServePositivePercent(Math.round( (int)(( (float)(player.getPlayerStats().getServePositive())/(player.getPlayerStats().getServeAttempts())) * 100)));

                            //to samo dla drużyny
                           homeTeam.getTeamStats().setServePositivePercent(Math.round( (int)(( (float)(homeTeam.getTeamStats().getServePositive())/(homeTeam.getTeamStats().getServeAttempts())) * 100)));
                        }
                        // Obliczanie procentowej skutecznosci przyjęcia
                        if(player.getPlayerStats().getReceptionAttempts() != 0 ) {
                            player.getPlayerStats().setReceptionPositivePercent(Math.round( (int)(( (float)( player.getPlayerStats().getReceptionPositive() )/(player.getPlayerStats().getReceptionAttempts())) * 100)));
                            player.getPlayerStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(player.getPlayerStats().getReceptionPerfect())/(player.getPlayerStats().getReceptionAttempts())) * 100)));

                            //to samo dla drużyny
                            homeTeam.getTeamStats().setReceptionPositivePercent(Math.round( (int)(( (float)( homeTeam.getTeamStats().getReceptionPositive() )/(homeTeam.getTeamStats().getReceptionAttempts())) * 100)));
                            homeTeam.getTeamStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(homeTeam.getTeamStats().getReceptionPerfect())/(homeTeam.getTeamStats().getReceptionAttempts())) * 100)));


                        }
                        // Obliczanie procentowej skutecznosci ataku
                        if(player.getPlayerStats().getAttackAttempts() != 0 ) {
                            player.getPlayerStats().setAttackFinishedPercent(Math.round( (int)(( (float)( player.getPlayerStats().getAttackFinished() )/(player.getPlayerStats().getAttackAttempts())) * 100)));

                            //to samo dla drużyny
                            homeTeam.getTeamStats().setAttackFinishedPercent(Math.round( (int)(( (float)( homeTeam.getTeamStats().getAttackFinished() )/(homeTeam.getTeamStats().getAttackAttempts())) * 100)));

                        }

                        // Obliczanie ratio punktów (do uzupełnienia o pozostałe elementy
                        player.getPlayerStats().setPointsRatio(player.getPlayerStats().getPointsTotal() - player.getPlayerStats().getServeErrors() - player.getPlayerStats().getReceptionErrors() - player.getPlayerStats().getAttackErrors() - player.getPlayerStats().getAttackBlocked());

                        // To samo do podsumowania dla drużyny
                        homeTeam.getTeamStats().setPointsRatio(homeTeam.getTeamStats().getPointsTotal() - homeTeam.getTeamStats().getServeErrors() - homeTeam.getTeamStats().getReceptionErrors() - homeTeam.getTeamStats().getAttackErrors() - homeTeam.getTeamStats().getAttackBlocked());

                    }
                }
            } else {

                for (Player playerAway : awayTeam.getPlayerList()) {

                    if(playerAway.getNumber() == Integer.parseInt(data[1])){

                        // Parametry zagrywki
                        if (data[2].equals("S") && data[4].equals("#")){
                            playerAway.getPlayerStats().setServeAce((playerAway.getPlayerStats().getServeAce())+1);
                            playerAway.getPlayerStats().setServePositive((playerAway.getPlayerStats().getServePositive())+1);
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);
                            playerAway.getPlayerStats().setPointsTotal((playerAway.getPlayerStats().getPointsTotal())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServeAce((awayTeam.getTeamStats().getServeAce())+1 );
                            awayTeam.getTeamStats().setServePositive((awayTeam.getTeamStats().getServePositive())+1 );
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );
                            awayTeam.getTeamStats().setPointsTotal((awayTeam.getTeamStats().getPointsTotal())+1 );

                        } else if (data[2].equals("S") && data[4].equals("+")){
                            playerAway.getPlayerStats().setServePositive((playerAway.getPlayerStats().getServePositive())+1);
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServePositive((awayTeam.getTeamStats().getServePositive())+1 );
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("!")){
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("-")){
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("/")){
                            playerAway.getPlayerStats().setServePositive((playerAway.getPlayerStats().getServePositive())+1);
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServePositive((awayTeam.getTeamStats().getServePositive())+1 );
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );

                        } else if (data[2].equals("S") && data[4].equals("=")){
                            playerAway.getPlayerStats().setServeErrors((playerAway.getPlayerStats().getServeErrors())+1);
                            playerAway.getPlayerStats().setServeAttempts((playerAway.getPlayerStats().getServeAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setServeErrors((awayTeam.getTeamStats().getServeErrors())+1 );
                            awayTeam.getTeamStats().setServeAttempts((awayTeam.getTeamStats().getServeAttempts())+1 );
                        }

                        // Parametry przyjęcia
                        if (data[2].equals("R") && data[4].equals("#")){
                            playerAway.getPlayerStats().setReceptionPerfect((playerAway.getPlayerStats().getReceptionPerfect())+1);
                            playerAway.getPlayerStats().setReceptionPositive((playerAway.getPlayerStats().getReceptionPositive())+1);
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionPerfect((awayTeam.getTeamStats().getReceptionPerfect())+1);
                            awayTeam.getTeamStats().setReceptionPositive((awayTeam.getTeamStats().getReceptionPositive())+1);
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);


                        } else if (data[2].equals("R") && data[4].equals("+")){
                            playerAway.getPlayerStats().setReceptionPositive((playerAway.getPlayerStats().getReceptionPositive())+1);
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionPositive((awayTeam.getTeamStats().getReceptionPositive())+1);
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("!")){
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("-")){
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("/")){
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);

                        } else if (data[2].equals("R") && data[4].equals("=")){
                            playerAway.getPlayerStats().setReceptionErrors((playerAway.getPlayerStats().getReceptionErrors())+1);
                            playerAway.getPlayerStats().setReceptionAttempts((playerAway.getPlayerStats().getReceptionAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setReceptionErrors((awayTeam.getTeamStats().getReceptionErrors())+1);
                            awayTeam.getTeamStats().setReceptionAttempts((awayTeam.getTeamStats().getReceptionAttempts())+1);
                        }

                        // Parametry ataku
                        if (data[2].equals("A") && data[4].equals("#")){
                            playerAway.getPlayerStats().setAttackFinished((playerAway.getPlayerStats().getAttackFinished())+1);
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);
                            playerAway.getPlayerStats().setPointsTotal((playerAway.getPlayerStats().getPointsTotal())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackFinished((awayTeam.getTeamStats().getAttackFinished())+1);
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);
                            awayTeam.getTeamStats().setPointsTotal((awayTeam.getTeamStats().getPointsTotal())+1);


                        } else if (data[2].equals("A") && data[4].equals("+")){
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("!")){
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("-")){
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("/")){
                            playerAway.getPlayerStats().setAttackBlocked((playerAway.getPlayerStats().getAttackBlocked())+1);
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackBlocked((awayTeam.getTeamStats().getAttackBlocked())+1);
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);

                        } else if (data[2].equals("A") && data[4].equals("=")){
                            playerAway.getPlayerStats().setAttackErrors((playerAway.getPlayerStats().getAttackErrors())+1);
                            playerAway.getPlayerStats().setAttackAttempts((playerAway.getPlayerStats().getAttackAttempts())+1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setAttackErrors((awayTeam.getTeamStats().getAttackErrors())+1);
                            awayTeam.getTeamStats().setAttackAttempts((awayTeam.getTeamStats().getAttackAttempts())+1);
                        }

                        // Parametry bloku
                        if (data[2].equals("B") && data[4].equals("#")) {
                            playerAway.getPlayerStats().setBlockScore((playerAway.getPlayerStats().getBlockScore()) + 1);
                            playerAway.getPlayerStats().setPointsTotal((playerAway.getPlayerStats().getPointsTotal()) + 1);

                            // To samo do podsumowania dla drużyny
                            awayTeam.getTeamStats().setBlockScore((awayTeam.getTeamStats().getBlockScore()) + 1);
                            awayTeam.getTeamStats().setPointsTotal((awayTeam.getTeamStats().getPointsTotal()) + 1);
                        }

                        // Obliczanie procentowej skutecznosci zagrywki
                        if(playerAway.getPlayerStats().getServeAttempts() != 0 ) {
                            playerAway.getPlayerStats().setServePositivePercent(Math.round( (int)(( (float)(playerAway.getPlayerStats().getServePositive())/(playerAway.getPlayerStats().getServeAttempts())) * 100)));

                            //to samo dla drużyny
                            awayTeam.getTeamStats().setServePositivePercent(Math.round( (int)(( (float)(awayTeam.getTeamStats().getServePositive())/(awayTeam.getTeamStats().getServeAttempts())) * 100)));
                        }
                        // Obliczanie procentowej skutecznosci przyjęcia
                        if(playerAway.getPlayerStats().getReceptionAttempts() != 0 ) {
                            playerAway.getPlayerStats().setReceptionPositivePercent(Math.round( (int)(( (float)( playerAway.getPlayerStats().getReceptionPositive() )/(playerAway.getPlayerStats().getReceptionAttempts())) * 100)));
                            playerAway.getPlayerStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(playerAway.getPlayerStats().getReceptionPerfect())/(playerAway.getPlayerStats().getReceptionAttempts())) * 100)));

                            //to samo dla drużyny
                            awayTeam.getTeamStats().setReceptionPositivePercent(Math.round( (int)(( (float)( awayTeam.getTeamStats().getReceptionPositive() )/(awayTeam.getTeamStats().getReceptionAttempts())) * 100)));
                            awayTeam.getTeamStats().setReceptionPerfectPercent(Math.round( (int)(( (float)(awayTeam.getTeamStats().getReceptionPerfect())/(awayTeam.getTeamStats().getReceptionAttempts())) * 100)));


                        }
                        // Obliczanie procentowej skutecznosci ataku
                        if(playerAway.getPlayerStats().getAttackAttempts() != 0 ) {
                            playerAway.getPlayerStats().setAttackFinishedPercent(Math.round( (int)(( (float)( playerAway.getPlayerStats().getAttackFinished() )/(playerAway.getPlayerStats().getAttackAttempts())) * 100)));

                            //to samo dla drużyny
                            awayTeam.getTeamStats().setAttackFinishedPercent(Math.round( (int)(( (float)( awayTeam.getTeamStats().getAttackFinished() )/(awayTeam.getTeamStats().getAttackAttempts())) * 100)));

                        }

                        // Obliczanie ratio punktów (do uzupełnienia o pozostałe elementy
                        playerAway.getPlayerStats().setPointsRatio(playerAway.getPlayerStats().getPointsTotal() - playerAway.getPlayerStats().getServeErrors() - playerAway.getPlayerStats().getReceptionErrors() - playerAway.getPlayerStats().getAttackErrors() - playerAway.getPlayerStats().getAttackBlocked());

                        // To samo do podsumowania dla drużyny
                        awayTeam.getTeamStats().setPointsRatio(awayTeam.getTeamStats().getPointsTotal() - awayTeam.getTeamStats().getServeErrors() - awayTeam.getTeamStats().getReceptionErrors() - awayTeam.getTeamStats().getAttackErrors() - awayTeam.getTeamStats().getAttackBlocked());

                    }
                }

            }
            }
        }


    }







