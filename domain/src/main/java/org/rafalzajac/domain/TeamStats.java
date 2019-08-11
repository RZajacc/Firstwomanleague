package org.rafalzajac.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Embeddable
@Getter @Setter
public class TeamStats {


    /**
     * All points scored by the player and their ratio meaning Points total - points lost (errors in all elements)
     */
    private int pointsTotal;
    private int pointsRatio;

    /**
     * Serve data section
     */
    private int serveAttempts;
    private int serveAce;
    private int servePositive;
    private int servePositivePercent;
    private int serveErrors;

    /**
     * Recption data section
     */
    private int receptionAttempts;
    private int receptionErrors;
    private int receptionPositive;
    private int receptionPerfect;
    private int receptionPositivePercent;
    private int receptionPerfectPercent;

    /**
     * Attack data section
     */
    private int attackAttempts;
    private int attackErrors ;
    private int attackBlocked;
    private int attackFinished;
    private int attackFinishedPercent;

    /**
     * Block data section
     */
    private int blockScore ;

    /**
     * Summary data for team's result in the league section
     */
    private int matchPlayed;
    private int leaguePoints;
    private int matchWon;
    private int matchLost;
    private int setsWon;
    private int setsLost;
    private float setRatio;
    private int pointsWon;
    private int pointsLost;
    private float teamPointsRatio;

    /**
     * No argument constructor for factory method
     */
    private TeamStats () {

    }
    
    /**
     * Factory method for no argument constructor of TeamStats class
     * @return TeamStats object with default values
     */
    public static TeamStats create() {
        return new TeamStats();
    }
}