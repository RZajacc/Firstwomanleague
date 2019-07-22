package org.rafalzajac.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@NoArgsConstructor
@Data
public class PlayerStats {


    //Starting rotation
    private String startingRotS1;
    private String startingRotS2;
    private String startingRotS3;
    private String startingRotS4;
    private String startingRotS5;

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

    @OneToOne
    private Player player;

    public PlayerStats(Player player) {
        this.player = player;
    }
}
