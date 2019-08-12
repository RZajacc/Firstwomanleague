package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LeagueTest {

    private League leagueNoArgs;
    private League leagueAllArgs;

    @Before
    public void before() {
        leagueNoArgs = League.createNoArgs();
        leagueAllArgs = League.createAllArgs("1 Liga Kobiet", "2019/2020");
    }

    @Test
    public void createNoArgs() {
        assertThat(leagueNoArgs.getId()).isNull();
        assertThat(leagueNoArgs.getLeagueName()).isNull();
        assertThat(leagueNoArgs.getSeason()).isNull();
        assertThat(leagueNoArgs.getRoundList()).isNullOrEmpty();
    }

    @Test
    public void createAllArgs() {
        assertThat(leagueAllArgs.getId()).isNull();
        assertThat(leagueAllArgs.getLeagueName()).isEqualTo("1 Liga Kobiet");
        assertThat(leagueAllArgs.getSeason()).isEqualTo("2019/2020");
        assertThat(leagueAllArgs.getRoundList()).isNullOrEmpty();
    }

    @Test
    public void setValidLeagueName() {
        leagueNoArgs.setLeagueName("Liga Siatkowki Kobiet");
        assertThat(leagueNoArgs.getLeagueName()).isEqualTo("Liga Siatkowki Kobiet");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyStringAsLeagueName() {
        leagueNoArgs.setLeagueName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullAsLeagueName() {
        leagueNoArgs.setLeagueName(null);
    }

    @Test
    public void setValidSeasonName() {
        leagueNoArgs.setSeason("2020/2021");
        assertThat(leagueNoArgs.getSeason()).isEqualTo("2020/2021");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyStringAsSeasonName() {
        leagueNoArgs.setSeason("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullValueAsSeasonName() {
        leagueNoArgs.setSeason(null);
    }

    @Test
    public void setValidRoundList() {
        List<Round> roundList = new ArrayList<>();
        leagueNoArgs.setRoundList(roundList);
        assertThat(leagueNoArgs.getRoundList()).isSameAs(roundList);
    }

    @Test
    public void checkRoundListReference() {
        List<Round> roundList = new ArrayList<>();
        assertThat(leagueAllArgs.getRoundList()).isNotSameAs(roundList);
    }

    @Test(expected = NullPointerException.class)
    public void setNullReferenceAsRoundList() {
        leagueNoArgs.setRoundList(null);
    }
}
