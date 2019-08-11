package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    private Team teamNoArgs;
    private Team teamOneCoach;
    private Team teamBothCoaches;

    @Before
    public void before() {
        teamNoArgs = Team.createNoArgs();
        teamOneCoach = Team.createFirstCoachOnly("MKS", "MKS SomeTown", "John Doe");
        teamBothCoaches = Team.createBothCoaches("MKS", "MKS SomeTown", "John Doe", "Vince Doe");
    }

    @Test
    public void createWithNoArgs() {
        assertThat(teamNoArgs.getFirstCoach()).isNull();
    }

    @Test
    public void createWithOneCoach() {
        assertThat(teamOneCoach.getFirstCoach()).isEqualTo("John Doe");
    }

    @Test
    public void createWithBothCoaches() {
        assertThat(teamBothCoaches.getFirstCoach()).isEqualTo("John Doe");
    }

    @Test
    public void setValidTeamTag() {
        teamNoArgs.setTeamTag("MKS");
        assertThat(teamNoArgs.getTeamTag()).isEqualTo("MKS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullTeamTag() {
        teamNoArgs.setTeamTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyTeamTag() {
        teamNoArgs.setTeamTag("");
    }

    @Test
    public void setValidTeamName() {
        teamNoArgs.setTeamName("MKS Dabrowa");
        assertThat(teamNoArgs.getTeamName()).isEqualTo("MKS Dabrowa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullTeamName() {
        teamNoArgs.setTeamName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyTeamName() {
        teamNoArgs.setTeamName("");
    }

    @Test
    public void setValidFirstCoach() {
        teamNoArgs.setFirstCoach("John Doe");
        assertThat(teamNoArgs.getFirstCoach()).isEqualTo("John Doe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullFirstCoach() {
        teamNoArgs.setFirstCoach(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyFirstCoach() {
        teamNoArgs.setFirstCoach("");
    }

    @Test
    public void setValidSecondCoach() {
        teamNoArgs.setSecondCoach("Vince Doe");
        assertThat(teamNoArgs.getSecondCoach()).isEqualTo("Vince Doe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullSecondCoach() {
        teamNoArgs.setSecondCoach(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptySecondCoach() {
        teamNoArgs.setSecondCoach("");
    }

    @Test
    public void setValidFacebookAdress() {
        teamNoArgs.setFacebook("https://www.facebook.com/adress/");
        assertThat(teamNoArgs.getFacebook()).isEqualTo("https://www.facebook.com/adress/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullFacebookAdress() {
        teamNoArgs.setFacebook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyFacebookAdress() {
        teamNoArgs.setFacebook("");
    }

    @Test
    public void setValidWebPageAdress() {
        teamNoArgs.setWebPage("http://www.adress.pl/");
        assertThat(teamNoArgs.getWebPage()).isEqualTo("http://www.adress.pl/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullWebPageAdress() {
        teamNoArgs.setWebPage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyWebPageAdress() {
        teamNoArgs.setWebPage("");
    }


}
