package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundTest {

    private Round roundNoArgs;
    private Round roundAllArgs;

    @Before
    public void before() {
        League league =  new League("1 Liga Kobiet", "2019/2020");
        roundNoArgs = Round.createNoArgs();
        roundAllArgs = Round.createWithAllArguments(1, league);
    }

    @Test
    public void createWithNoArgumentConstructor() {
        assertThat(roundNoArgs.getId()).isNull();
        assertThat(roundNoArgs.getLeague()).isNull();
        assertThat(roundNoArgs.getMatchList()).isNullOrEmpty();
        assertThat(roundNoArgs.getRoundNumber()).isEqualTo(0);
    }

    @Test
    public void createWithAllArgumentsConstructor() {
        assertThat(roundAllArgs.getId()).isNull();
        assertThat(roundAllArgs.getLeague()).isNotNull();
        assertThat(roundAllArgs.getMatchList()).isNullOrEmpty();
        assertThat(roundAllArgs.getRoundNumber()).isEqualTo(1);
    }

    @Test
    public void setValidRoundNumber() {
        roundAllArgs.setRoundNumber(5);
        assertThat(roundAllArgs.getRoundNumber()).isEqualTo(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooHighRoundNumber() {
        roundAllArgs.setRoundNumber(51);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setZeroRoundNumber() {
        roundAllArgs.setRoundNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeRoundNumber() {
        roundAllArgs.setRoundNumber(-1);
    }

    @Test
    public void setLeague() {
        League league = new League();
        roundAllArgs.setLeague(league);
        assertThat(roundAllArgs.getLeague()).isSameAs(league);
    }

    @Test
    public void checkLeagueReferences() {
        League league = new League();
        assertThat(roundAllArgs.getLeague()).isNotSameAs(league);
    }

    @Test(expected = NullPointerException.class)
    public void setNullLeague() {
        roundAllArgs.setLeague(null);
    }

    @Test
    public void setMatchList() {
        List<Game> matchList = new ArrayList<>();
        roundAllArgs.setMatchList(matchList);
    }

    @Test
    public void checkMatchListReferences() {
        List<Game> matchList = new ArrayList<>();
        assertThat(roundAllArgs.getMatchList()).isNotSameAs(matchList);
    }

    @Test(expected = NullPointerException.class)
    public void setNullMatchList() {
        roundAllArgs.setMatchList(null);
    }
}
