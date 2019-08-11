package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamStatsTest {

    private TeamStats teamStats;

    @Before
    public void before() {
        teamStats = TeamStats.create();
    }

    @Test
    public void setIntegerValues() {
        teamStats.setAttackAttempts(15);
        assertThat(teamStats.getAttackAttempts()).isEqualTo(15);
    }

    @Test
    public void setFloatValues() {
        teamStats.setSetRatio(1.5f);
        teamStats.setPointsRatio(152/178);
        assertThat(teamStats.getSetRatio()).isEqualTo(1.5f);
        assertThat(teamStats.getPointsRatio()).isEqualTo(152/178);
    }
}
