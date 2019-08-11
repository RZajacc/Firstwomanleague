package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameResultTest {

    private GameResult gameResult;

    @Before
    public void before() {
        gameResult = GameResult.create();
    }

    @Test
    public void create() {
        assertThat(gameResult.getHomeTeamSetsWon()).isEqualTo(0);
        assertThat(gameResult.getAwayTeamSetsWon()).isEqualTo(0);
        assertThat(gameResult.getHomeTeamSet1Score()).isEqualTo(0);
        assertThat(gameResult.getAwayTeamSet1Score()).isEqualTo(0);
    }

    @Test
    public void assignValidSetWonNumber() {
        gameResult.setHomeTeamSetsWon(3);
        assertThat(gameResult.getHomeTeamSetsWon()).isEqualTo(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignNegativeSetsWon(){
        gameResult.setHomeTeamSetsWon(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignToHighSetsWon() {
        gameResult.setHomeTeamSetsWon(4);
    }

    @Test
    public void assignValidSetScore() {
        gameResult.setHomeTeamSet1Score(25);
        assertThat(gameResult.getHomeTeamSet1Score()).isEqualTo(25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignToHighSetScore(){
        gameResult.setHomeTeamSet1Score(50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignNegativeSetScore() {
        gameResult.setHomeTeamSet1Score(-1);
    }

}
