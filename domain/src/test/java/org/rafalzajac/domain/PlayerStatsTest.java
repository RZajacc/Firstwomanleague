package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Since there is a lot of fields in this class, and no specific criteria for values I decided to test only first of a
 * specific type. So one value from Strings and one from integers. PlayerStats in this application are updated only based
 * on data from a file that is why I decided not to constrain input in any way.
 */
public class PlayerStatsTest {

    private PlayerStats playerStats;

    @Before
    public void before () {
        playerStats = PlayerStats.create();
    }

    @Test
    public void checkDefaultData() {
        assertThat(playerStats.getStartingRotS1()).isNull();
        assertThat(playerStats.getPointsTotal()).isEqualTo(0);
    }

    @Test
    public void setPlayerStartingRotation() {
        playerStats.setStartingRotS1("");
        playerStats.setStartingRotS2("1");
        playerStats.setStartingRotS3("*");

        assertThat(playerStats.getStartingRotS1()).isEqualTo("");
        assertThat(playerStats.getStartingRotS2()).isEqualTo("1");
        assertThat(playerStats.getStartingRotS3()).isEqualTo("*");
    }

    @Test
    public void setStatsData() {
        playerStats.setPointsTotal(15);
        playerStats.setPointsRatio(7);

        assertThat(playerStats.getPointsTotal()).isEqualTo(15);
        assertThat(playerStats.getPointsRatio()).isEqualTo(7);
    }


}
