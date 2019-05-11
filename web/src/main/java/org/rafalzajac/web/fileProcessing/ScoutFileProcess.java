package org.rafalzajac.web.fileProcessing;


import lombok.Data;
import org.aspectj.apache.bcel.util.Play;
import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.Player;
import org.rafalzajac.domain.PlayerStats;
import org.rafalzajac.domain.Team;
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
            homeTeam = new Team(data[0], data[1], data[2], data[3]);
            String[] data1 = teamData.get(1).split(";");
            awayTeam = new Team(data1[0], data1[1], data1[2], data1[3]);
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

            if (data[0].equals("*")){

                for (Player player : homeTeam.getPlayerList()) {
                    if(player.getNumber() == Integer.parseInt(data[1])){

                        // Parametry zagrywki
                        if (data[2].equals("S") && data[4].equals("#")){
                            player.getPlayerStats().setServeAce((player.getPlayerStats().getServeAce())+1);
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                            player.getPlayerStats().setPointsTotal((player.getPlayerStats().getPointsTotal())+1);
                        } else if (data[2].equals("S") && data[4].equals("+")){
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                        } else if (data[2].equals("S") && data[4].equals("!")){
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                        } else if (data[2].equals("S") && data[4].equals("-")){
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                        } else if (data[2].equals("S") && data[4].equals("/")){
                            player.getPlayerStats().setServePositive((player.getPlayerStats().getServePositive())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                        } else if (data[2].equals("S") && data[4].equals("=")){
                            player.getPlayerStats().setServeErrors((player.getPlayerStats().getServeErrors())+1);
                            player.getPlayerStats().setServeAttempts((player.getPlayerStats().getServeAttempts())+1);
                        }


                        // Obliczanie procentowej skutecznosci zagrywki
                        if(player.getPlayerStats().getServeAttempts() != 0 ) {
                            player.getPlayerStats().setServePositivePercent(Math.round( (int)(( (float)(player.getPlayerStats().getServePositive())/(player.getPlayerStats().getServeAttempts())) * 100)));
                        }

                        // Obliczanie ratio punktów (do uzupełnienia o pozostałe elementy
                        player.getPlayerStats().setPointsRatio(player.getPlayerStats().getPointsTotal() - player.getPlayerStats().getServeErrors());

                    }

                }

            }
        }
    }

    }





