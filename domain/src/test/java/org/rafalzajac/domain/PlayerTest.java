package org.rafalzajac.domain;

import org.aspectj.apache.bcel.util.Play;
import org.junit.Before;
import org.junit.Test;

import javax.xml.validation.Validator;

import static org.junit.Assert.*;

public class PlayerTest {

    @Before
    public void before() {
        player = new Player();
        team = new Team();
        playerStats = new PlayerStats();
    }

    @Test
    public void createWithEmptyConstructor() {

        assertNull(player.getId());
        assertEquals(0, player.getNumber());
        assertEquals(0, player.getAge());
        assertEquals(0, player.getHeight());
        assertNull(player.getPlayerTag());
        assertNull(player.getFirstName());
        assertNull(player.getLastName());
        assertNull(player.getPosition());
        assertNull(player.getTeam());
        assertNotNull(player.getPlayerStats());
    }

    @Test
    public void setValidPlayerNumber(){
        player.setNumber(16);
        assertEquals(16, player.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativePlayerNumber() {
        player.setNumber(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setZeroAsPlayerNumber() {
        player.setNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooHighPlayerNumber(){
        player.setNumber(200);
    }

    @Test
    public void setValidPlayerHeight() {
        player.setHeight(186);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooLowPlayerHeight() {
        player.setHeight(99);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTooHighPlayerHeight() {
        player.setHeight(251);
    }

    @Test
    public void setValidPlayerFirstName() {
        player.setFirstName("Stefan");
        assertEquals("Stefan", player.getFirstName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyPlayerName() {
        player.setFirstName("");
    }

    @Test(expected = NullPointerException.class)
    public void setNullPlayerName() {
        player.setFirstName(null);
    }

    @Test
    public void setValidTeam() {
        player.setTeam(team);
        assertSame(team, player.getTeam());
    }

    @Test(expected = IllegalArgumentException.class)
    public void SetNullAsTeam(){
        player.setTeam(null);
    }

    @Test
    public void setValidStatsDetails() {
        player.getPlayerStats().setAttackAttempts(15);
        assertEquals(15, player.getPlayerStats().getAttackAttempts());
    }

    @Test
    public void setValidStats() {
        player.setPlayerStats(playerStats);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullPlayerStats() {
        player.setPlayerStats(null);
    }


    private Player player;
    private Team team;
    private PlayerStats playerStats;
}
