package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@NoArgsConstructor
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

    @ManyToOne
    private Match match;

    @OneToMany(mappedBy = "team")
    private List<Player> playerList;


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
}
