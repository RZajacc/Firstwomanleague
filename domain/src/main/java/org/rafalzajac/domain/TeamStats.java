package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@Getter @Setter
public class TeamStats {

    @Id @GeneratedValue
    private Long id;

    //Points
    private int pointsTotal = 0;
    private int pointsRatio = 0;

    //Serve
    private int serveAttempts = 0;
    private int serveAce = 0;
    private int servePositive = 0;
    private int servePositivePercent = 0;
    private int serveErrors = 0;

    //Reception
    private int receptionAttempts = 0;
    private int receptionErrors = 0;
    private int receptionPositive = 0;
    private int receptionPerfect = 0;
    private int receptionPositivePercent = 0;
    private int receptionPerfectPercent = 0;

    //Attack
    private int attackAttempts = 0;
    private int attackErrors = 0;
    private int attackBlocked = 0;
    private int attackFinished = 0;
    private int attackFinishedPercent = 0;

    //Block
    private int blockScore = 0;


    //Overall stats for team
    private int matchPlayed = 0;
    private int leaguePoints = 0;
    private int matchWon = 0;
    private int matchLost = 0;
    private int setsWon = 0;
    private int setsLost = 0;
    private float setRatio = 0;
    private int pointsWon = 0;
    private int pointsLost = 0;
    private float teamPointsRatio = 0;




    @OneToOne
    private Team team;

    public TeamStats(Team team) {
        this.team = team;
    }
}