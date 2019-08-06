package org.rafalzajac.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
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
    private int attackErrors;
    private int attackBlocked;
    private int attackFinished;
    private int attackFinishedPercent;

    //Block
    private int blockScore;

}
