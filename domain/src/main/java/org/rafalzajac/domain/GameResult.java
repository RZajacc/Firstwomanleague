package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Embeddable
@Getter
public class GameResult {

    /**
     * Values for sets won by each team during game
     */
    private int homeTeamSetsWon;
    private int awayTeamSetsWon;

    /**
     * Home team result in each set
     */
    private int homeTeamSet1Score;
    private int homeTeamSet2Score;
    private int homeTeamSet3Score;
    private int homeTeamSet4Score;
    private int homeTeamSet5Score;

    /**
     * Away team result in each set
     */
    private int awayTeamSet1Score;
    private int awayTeamSet2Score;
    private int awayTeamSet3Score;
    private int awayTeamSet4Score;
    private int awayTeamSet5Score;

    /**
     * No argument constructor used for factory method
     */
    private GameResult() {

    }

    /**
     * Setter methods for sets won by each team
     * @param homeTeamSetsWon - should be a number between 0 and 3
     */
    public void setHomeTeamSetsWon(int homeTeamSetsWon) {
        Preconditions.checkArgument(homeTeamSetsWon >= 0 && homeTeamSetsWon <=3, "Sets won can vary between 0-3");
        this.homeTeamSetsWon = homeTeamSetsWon;
    }

    public void setAwayTeamSetsWon(int awayTeamSetsWon) {
        Preconditions.checkArgument(awayTeamSetsWon >= 0 && awayTeamSetsWon <=3, "Sets won can vary between 0-3");
        this.awayTeamSetsWon = awayTeamSetsWon;
    }

    /**
     * Setter methods for each set score by each team. Number should be between 0-50. 0 can be assigned when its walkower. Highest score
     * in official match is 44-42 so I decided to make a limit to 50.
     * @param homeTeamSet1Score
     */
    public void setHomeTeamSet1Score(int homeTeamSet1Score) {
        Preconditions.checkArgument(homeTeamSet1Score >= 0 && homeTeamSet1Score < 50);
        this.homeTeamSet1Score = homeTeamSet1Score;
    }

    public void setHomeTeamSet2Score(int homeTeamSet2Score) {
        Preconditions.checkArgument(homeTeamSet2Score >= 0 && homeTeamSet2Score < 50);
        this.homeTeamSet2Score = homeTeamSet2Score;
    }

    public void setHomeTeamSet3Score(int homeTeamSet3Score) {
        Preconditions.checkArgument(homeTeamSet3Score >= 0 && homeTeamSet3Score < 50);
        this.homeTeamSet3Score = homeTeamSet3Score;
    }

    public void setHomeTeamSet4Score(int homeTeamSet4Score) {
        Preconditions.checkArgument(homeTeamSet4Score >= 0 && homeTeamSet4Score < 50);
        this.homeTeamSet4Score = homeTeamSet4Score;
    }

    public void setHomeTeamSet5Score(int homeTeamSet5Score) {
        Preconditions.checkArgument(homeTeamSet5Score >= 0 && homeTeamSet5Score < 50);
        this.homeTeamSet5Score = homeTeamSet5Score;
    }

    public void setAwayTeamSet1Score(int awayTeamSet1Score) {
        Preconditions.checkArgument(awayTeamSet1Score >= 0 && awayTeamSet1Score < 50);
        this.awayTeamSet1Score = awayTeamSet1Score;
    }

    public void setAwayTeamSet2Score(int awayTeamSet2Score) {
        Preconditions.checkArgument(awayTeamSet2Score >= 0 && awayTeamSet2Score < 50);
        this.awayTeamSet2Score = awayTeamSet2Score;
    }

    public void setAwayTeamSet3Score(int awayTeamSet3Score) {
        Preconditions.checkArgument(awayTeamSet3Score >= 0 && awayTeamSet3Score < 50);
        this.awayTeamSet3Score = awayTeamSet3Score;
    }

    public void setAwayTeamSet4Score(int awayTeamSet4Score) {
        Preconditions.checkArgument(awayTeamSet4Score >= 0 && awayTeamSet4Score < 50);
        this.awayTeamSet4Score = awayTeamSet4Score;
    }

    public void setAwayTeamSet5Score(int awayTeamSet5Score) {
        Preconditions.checkArgument(awayTeamSet5Score >= 0 && awayTeamSet5Score < 50);
        this.awayTeamSet5Score = awayTeamSet5Score;
    }

    /**
     * Factory method for GameResult.
     * @return - instance of GameResult class with default values
     */
    public static GameResult create() {
        return new GameResult();
    }

}
