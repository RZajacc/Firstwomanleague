package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Embeddable
@Getter @Setter
@NoArgsConstructor
public class PlayerStats {


    /**
     * It is represented by one number or symbol in DataVolley file. It is either a number representing starting zone
     * for a player (1-6) if he starts set in first squad or "*" symbol when he enters the court during set. If a player
     * remains on substitute bench then this field is left blank
     */
    private String startingRotS1;
    private String startingRotS2;
    private String startingRotS3;
    private String startingRotS4;
    private String startingRotS5;

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
     * Reception data section
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
    private int attackErrors;
    private int attackBlocked;
    private int attackFinished;
    private int attackFinishedPercent;

    /**
     * Block data section
     */
    private int blockScore;


    /**
     * Factory method for no argument constructor of PlayerStats class
     * @return PlayerStats object with default values
     */
    public static PlayerStats create() {
        return new PlayerStats();
    }
}
