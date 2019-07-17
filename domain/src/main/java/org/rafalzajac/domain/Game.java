package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter @Setter
public class Game implements Comparable<Game>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    private String scoutPath;
    private Boolean statsSaved;


    @ManyToOne
    Round round;

    @ManyToMany(mappedBy = "matchList")
    private List<Team> teams;

    @OneToOne
    private GameResult gameResult;

    public Game() {
        statsSaved = false;
    }

    public Game(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        teams = new LinkedList<>();
        statsSaved = false;
    }

    public Game(String homeTeam, String awayTeam, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        teams = new LinkedList<>();
        statsSaved = false;
    }

    public Game(String homeTeam, String awayTeam, int matchNumber, Round round, GameResult gameResult) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchNumber = matchNumber;
        this.round = round;
        this.gameResult = gameResult;
        teams = new LinkedList<>();
        statsSaved = false;
    }

    @Override
    public int compareTo(Game o) {
        return Integer.compare(this.getMatchNumber(), o.getMatchNumber());
    }
}
