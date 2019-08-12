package org.rafalzajac.domain.DTO_Object;

import org.junit.Before;
import org.junit.Test;
import org.rafalzajac.domain.GameResult;
import org.rafalzajac.domain.Round;
import org.rafalzajac.domain.Team;
import org.rafalzajac.dto_objects.GameDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameDTOTest {

    private GameDTO gameDTO;
    private Round round;

    @Before
    public void before() {
        gameDTO = GameDTO.create();
        round = Round.createNoArgs();
    }

    @Test
    public void create() {
        assertThat(gameDTO.getId()).isNull();
        assertThat(gameDTO.getHomeTeam()).isNull();
        assertThat(gameDTO.getAwayTeam()).isNull();
        assertThat(gameDTO.getMatchNumber()).isEqualTo(0);
        assertThat(gameDTO.getRound()).isNull();
        assertThat(gameDTO.getGameResult()).isNotNull();
        assertThat(gameDTO.getTeams()).isNullOrEmpty();
    }

    @Test
    public void setValidHomeTeam() {
        gameDTO.setHomeTeam("MKS Team");
        assertThat(gameDTO.getHomeTeam()).isEqualTo("MKS Team");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyStringAsHomeTeam() {
        gameDTO.setHomeTeam("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullValueAsHomeTeam() {
        gameDTO.setHomeTeam(null);
    }

    @Test
    public void setValidAwayTeam() {
        gameDTO.setAwayTeam("WKS Team");
        assertThat(gameDTO.getAwayTeam()).isEqualTo("WKS Team");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyStringAsAwayTeam() {
        gameDTO.setAwayTeam("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullValueAsAwayTeam() {
        gameDTO.setAwayTeam(null);
    }

    @Test
    public void setValidMatchNumber() {
        gameDTO.setMatchNumber(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setZeroMatchNumber() {
        gameDTO.setMatchNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setToHighMatchNumber() {
        gameDTO.setMatchNumber(301);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeMatchNumber() {
        gameDTO.setMatchNumber(-1);
    }

    //Correct
    @Test
    public void setValidRoundObject() {
        Round round = Round.createNoArgs();
        gameDTO.setRound(round);
        assertThat(gameDTO.getRound()).isSameAs(round);
    }

    @Test
    public void checkRoundReferences() {
        Round round = Round.createNoArgs();
        gameDTO.setRound(round);
        assertThat(gameDTO.getRound()).isNotSameAs(Round.createNoArgs());
    }

    @Test(expected = NullPointerException.class)
    public void setNullObjectAsRound() {
        gameDTO.setRound(null);
    }

    @Test
    public void setValidGameResultObject() {
        GameResult result = GameResult.create();
        gameDTO.setGameResult(result);
        assertThat(gameDTO.getGameResult()).isSameAs(result);
    }

    @Test
    public void checkGameResultReferences() {
        GameResult result = GameResult.create();
        gameDTO.setGameResult(result);
        assertThat(gameDTO.getGameResult()).isNotSameAs(GameResult.create());
    }

    @Test(expected = NullPointerException.class)
    public void setNullObjectAsGameResult() {
        gameDTO.setGameResult(null);
    }

    @Test
    public void setValidTeamsList() {
        List<Team> teamList = new ArrayList<>();
        gameDTO.setTeams(teamList);
        assertThat(gameDTO.getTeams()).isSameAs(teamList);
    }

    @Test
    public void checkTeamListReferences() {
        List<Team> teamList = new ArrayList<>();
        List<Team> teamList2 = new ArrayList<>();
        gameDTO.setTeams(teamList);
        assertThat(gameDTO.getTeams()).isNotSameAs(teamList2);
    }

    @Test(expected = NullPointerException.class)
    public void setNullAsTeamList() {
        gameDTO.setTeams(null);
    }
}
