package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String teamTag;
    private String teamName;
    private String firstCoach;
    private String secondCoach;

    @OneToMany(mappedBy = "team")
    private List<Player> playerList;

    @ManyToOne
    private Match match;

    public Team(String teamTag, String teamName, String firstCoach, String secondCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
    }
}
