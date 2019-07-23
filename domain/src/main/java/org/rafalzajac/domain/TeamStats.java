package org.rafalzajac.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Embeddable
@NoArgsConstructor
@Data
public class TeamStats {


    //Points
    private int pointsTotal;
    private int pointsRatio;

    //Serve
    private int serveAttempts;
    private int serveAce;
    private int servePositive;
    private int servePositivePercent;
    private int serveErrors;

    //Reception
    private int receptionAttempts;
    private int receptionErrors;
    private int receptionPositive;
    private int receptionPerfect;
    private int receptionPositivePercent;
    private int receptionPerfectPercent;

    //Attack
    private int attackAttempts;
    private int attackErrors ;
    private int attackBlocked;
    private int attackFinished;
    private int attackFinishedPercent;

    //Block
    private int blockScore ;


    //Overall stats for team
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

}