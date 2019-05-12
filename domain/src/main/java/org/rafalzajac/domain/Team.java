package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@AllArgsConstructor
@Getter @Setter
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String teamTag;
    private String teamName;
    private String firstCoach;
    private String secondCoach;
    private String webPage;
    private String facebook;

    @ManyToOne
    private Match match;

    @OneToMany(mappedBy = "team")
    private List<Player> playerList;

    @OneToOne
    private TeamStats teamStats;

    public Team () {
        playerList = new LinkedList<>();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        playerList = new LinkedList<>();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach, Match match) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        this.match = match;
        playerList = new LinkedList<>();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach, String webPage, String facebook, Match match) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        this.webPage = webPage;
        this.facebook = facebook;
        this.match = match;
        playerList = new LinkedList<>();
    }

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach, TeamStats teamStats) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        this.teamStats = teamStats;
        playerList = new LinkedList<>();
    }



    public void addPlayer (Player player) {
        playerList.add(player);
    }

}