package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    private Game game;

    @Before
    public void before() {
        game = Game.create();
    }

    @Test
    public void create() {
        assertThat(game.getAwayTeam()).isNull();
        assertThat(game.getAwayTeam()).isNull();
        assertThat(game.getMatchNumber()).isEqualTo(0);
        assertThat(game.getScoutPath()).isNull();
        assertThat(game.getStatsSaved()).isFalse();
        assertThat(game.getGameResult()).isNotNull();
        assertThat(game.getRound()).isNull();
        assertThat(game.getTeams()).isNullOrEmpty();
    }

    @Test
    public void assignValidTeam() {
        game.setHomeTeam("MKS Team");
        assertThat(game.getHomeTeam()).isEqualTo("MKS Team");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignEmptyStringAsTeam(){
        game.setHomeTeam("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignNullAsTeam(){
        game.setHomeTeam(null);
    }

    @Test
    public void setValidMatchNumber() {
        game.setMatchNumber(42);
        assertThat(game.getMatchNumber()).isEqualTo(42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setToHighMatchNumber() {
        game.setMatchNumber(301);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setZeroMatchNumber() {
        game.setMatchNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeMatchNumber() {
        game.setMatchNumber(-1);
    }

    @Test
    public void setValidScoutPath() {
        game.setScoutPath("C:/Users/Rafal/Match1.dvw");
        assertThat(game.getScoutPath()).isEqualTo("C:/Users/Rafal/Match1.dvw");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyScoutPath() {
        game.setScoutPath("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullScoutPath() {
        game.setScoutPath(null);
    }

    @Test
    public void changeStateOfStatsSaved() {
        game.setStatsSaved(true);
        assertThat(game.getStatsSaved()).isTrue();
    }

    @Test
    public void setValidGameResultObject() {
        GameResult result = GameResult.create();
        game.setGameResult(result);
        assertThat(game.getGameResult()).isSameAs(result);
    }

    @Test
    public void checkGameResultReferences() {
        GameResult result = GameResult.create();
        game.setGameResult(result);
        assertThat(game.getGameResult()).isNotSameAs(GameResult.create());
    }

    @Test(expected = NullPointerException.class)
    public void setNullObjectAsGameResult() {
        game.setGameResult(null);
    }

    @Test
    public void setValidRoundObject() {
        Round round = Round.createNoArgs();
        game.setRound(round);
        assertThat(game.getRound()).isSameAs(round);
    }

    @Test
    public void checkRoundReferences() {
        Round round = Round.createNoArgs();
        game.setRound(round);
        assertThat(game.getRound()).isNotSameAs(Round.createNoArgs());
    }

    @Test(expected = NullPointerException.class)
    public void setNullObjectAsRound() {
        game.setRound(null);
    }

    @Test
    public void setValidTeamsList() {
        List<Team> teamList = new ArrayList<>();
        game.setTeams(teamList);
        assertThat(game.getTeams()).isSameAs(teamList);
    }

    @Test
    public void checkTeamListReferences() {
        List<Team> teamList = new ArrayList<>();
        List<Team> teamList2 = new ArrayList<>();
        game.setTeams(teamList);
        assertThat(game.getTeams()).isNotSameAs(teamList2);
    }

    @Test(expected = NullPointerException.class)
    public void setNullAsTeamList() {
        game.setTeams(null);
    }

}
