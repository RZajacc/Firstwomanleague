package org.rafalzajac.domain;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {


    @Before
    public void before() {
        playerEmptyConstructor = new Player();
        team = new Team();
        playerNotEmptyConstructor = new Player(42, "Raf-Zaj", "Rafał", "Zając", team);
        playerStats = new PlayerStats();
    }

    @Test
    public void createWithEmptyConstructor() {

        assertThat(playerEmptyConstructor.getId()).isNull();
        assertThat(playerEmptyConstructor.getNumber()).isEqualTo(0);
        assertThat(playerEmptyConstructor.getAge()).isEqualTo(0);
        assertThat(playerEmptyConstructor.getHeight()).isEqualTo(0);
        assertThat(playerEmptyConstructor.getPlayerTag()).isNull();
        assertThat(playerEmptyConstructor.getFirstName()).isNull();
        assertThat(playerEmptyConstructor.getLastName()).isNull();
        assertThat(playerEmptyConstructor.getPosition()).isNull();
        assertThat(playerEmptyConstructor.getTeam()).isNull();
        assertThat(playerEmptyConstructor.getPlayerStats()).isNotNull();
    }

    @Test
    public void createWithNotEmptyConstructor() {
        assertThat(playerNotEmptyConstructor.getNumber()).isEqualTo(42);
        assertThat(playerNotEmptyConstructor.getPlayerTag()).isEqualTo("Raf-Zaj");
        assertThat(playerNotEmptyConstructor.getFirstName()).isEqualTo("Rafał");
        assertThat(playerNotEmptyConstructor.getLastName()).isEqualTo("Zając");
        assertThat(playerNotEmptyConstructor.getTeam()).isNotNull();
        assertThat(playerNotEmptyConstructor.getTeam()).isEqualTo(team);
    }

    @Test
    public void setValidPlayerNumber(){
        playerEmptyConstructor.setNumber(16);
        assertThat(playerEmptyConstructor.getNumber()).isEqualTo(16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativePlayerNumber() {
        playerEmptyConstructor.setNumber(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setZeroAsPlayerNumber() {
        playerEmptyConstructor.setNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooHighPlayerNumber(){
        playerEmptyConstructor.setNumber(200);
    }

    @Test
    public void setValidPlayerHeight() {
        playerEmptyConstructor.setHeight(186);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooLowPlayerHeight() {
        playerEmptyConstructor.setHeight(99);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooHighPlayerHeight() {
        playerEmptyConstructor.setHeight(251);
    }

    @Test
    public void setValidPlayerFirstName() {
        playerEmptyConstructor.setFirstName("Stefan");
        assertThat(playerEmptyConstructor.getFirstName()).isEqualTo("Stefan");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyPlayerName() {
        playerEmptyConstructor.setFirstName("");
    }

    @Test(expected = NullPointerException.class)
    public void setNullPlayerName() {
        playerEmptyConstructor.setFirstName(null);
    }

    @Test
    public void setValidTeam() {
        playerEmptyConstructor.setTeam(team);
        assertThat(playerEmptyConstructor.getTeam()).isSameAs(team);
    }

    @Test(expected = NullPointerException.class)
    public void SetNullAsTeam(){
        playerEmptyConstructor.setTeam(null);
    }

    @Test
    public void setValidStatsDetails() {
        playerEmptyConstructor.getPlayerStats().setAttackAttempts(15);
        assertThat(playerEmptyConstructor.getPlayerStats().getAttackAttempts()).isEqualTo(15);
    }

    @Test
    public void setValidStats() {
        playerEmptyConstructor.setPlayerStats(playerStats);
    }

    @Test(expected = NullPointerException.class)
    public void setNullPlayerStats() {
        playerEmptyConstructor.setPlayerStats(null);
    }

    @Test
    public void equalsVerify() {
        assertThat(playerEmptyConstructor).isNotEqualTo(new Player());
    }


    private Player playerEmptyConstructor;
    private Player playerNotEmptyConstructor;
    private Team team;
    private PlayerStats playerStats;
}
