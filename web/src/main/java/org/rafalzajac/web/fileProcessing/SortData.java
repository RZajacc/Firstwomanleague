package org.rafalzajac.web.fileProcessing;

import lombok.NoArgsConstructor;
import org.rafalzajac.domain.Player;
import org.rafalzajac.domain.Team;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class SortData {



    public List<Team> sortTeamTable (List<Team> teams) {
        teams.sort((t1, t2) -> {
            if ( ((t1.getTeamStats().getLeaguePoints() - t2.getTeamStats().getLeaguePoints() ) == 0) &&
                    ((t1.getTeamStats().getMatchWon() - t2.getTeamStats().getMatchWon()) == 0) &&
                    ((t1.getTeamStats().getSetRatio() - t2.getTeamStats().getSetRatio()) == 0) ) {
                return t2.getTeamStats().getPointsRatio() - t1.getTeamStats().getPointsRatio();
            } else if( ((t1.getTeamStats().getLeaguePoints() - t2.getTeamStats().getLeaguePoints() ) == 0) &&
                    ((t1.getTeamStats().getMatchWon() - t2.getTeamStats().getMatchWon()) == 0) ){
                return (int)(t2.getTeamStats().getSetRatio() - t1.getTeamStats().getSetRatio());
            } else if( (t1.getTeamStats().getLeaguePoints() - t2.getTeamStats().getLeaguePoints()) == 0 ){
                return t2.getTeamStats().getMatchWon() - t1.getTeamStats().getMatchWon();
            } else{
                return t2.getTeamStats().getLeaguePoints() - t1.getTeamStats().getLeaguePoints();
            }
        });

        return teams;
    }

    public List<Player> sortPlayerRankTable (List<Player> players, String text) {

        if (text.equals("alphabetically")){
            players.sort(Comparator.comparing(Player::getLastName));
        }else if (text.equals("club")){
            players.sort(Comparator.comparing(p -> p.getTeam().getTeamName()));
        } else if (text.equals("pointsTotal")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getPointsTotal() - p1.getPlayerStats().getPointsTotal()
            );
        } else if (text.equals("serveAce")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getServeAce() - p1.getPlayerStats().getServeAce()
            );
        }else if (text.equals("attackAttempt")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getAttackAttempts() - p1.getPlayerStats().getAttackAttempts()
            );
        }else if (text.equals("attackFinished")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getAttackFinished() - p1.getPlayerStats().getAttackFinished()
            );
        }else if (text.equals("attackFinishedPercent")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getAttackFinishedPercent() - p1.getPlayerStats().getAttackFinishedPercent()
            );
        }else if (text.equals("receptionAttempts")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getReceptionAttempts() - p1.getPlayerStats().getReceptionAttempts()
            );
        }else if (text.equals("receptionPositive")){
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getReceptionPositivePercent() - p1.getPlayerStats().getReceptionPositivePercent()
            );
        }else if (text.equals("receptrionPerfect")) {
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getReceptionPerfectPercent() - p1.getPlayerStats().getReceptionPerfectPercent()
            );
        }else {
            players.sort((p1, p2) ->
                    p2.getPlayerStats().getBlockScore() - p1.getPlayerStats().getBlockScore()
            );
        }

        return players;
    }
}
