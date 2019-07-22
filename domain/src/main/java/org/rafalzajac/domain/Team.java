package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Entity
@AllArgsConstructor
@Getter @Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String teamTag;
    private String teamName;
    private String firstCoach;
    private String secondCoach;
    private String webPage;
    private String facebook;

    @ManyToMany
    private List<Game> matchList;

    @OneToMany(mappedBy = "team")
    private List<Player> playerList;

    private TeamStats teamStats;

    public Team () {
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = new TeamStats();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = new TeamStats();
    }

    public Team(String teamTag, String teamName, String firstCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = new TeamStats();
    }

    public Team(String teamTag, String teamName, String firstCoach, TeamStats teamStats) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.teamStats = teamStats;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach, TeamStats teamStats) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        this.teamStats = teamStats;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
    }


    public Team(String teamTag, String teamName, String firstCoach, String secondCoach, String webPage, String facebook) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        this.webPage = webPage;
        this.facebook = facebook;
        matchList = new ArrayList<>();
        playerList = new LinkedList<>();
    }


    public void addPlayer (Player player) {
        playerList.add(player);
    }

}
