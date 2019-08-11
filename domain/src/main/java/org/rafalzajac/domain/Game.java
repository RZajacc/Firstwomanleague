package org.rafalzajac.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Data
public class Game implements Comparable<Game>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    private String scoutPath;
    private Boolean statsSaved;

    private GameResult gameResult;

    @ManyToOne
    Round round;

    @ManyToMany(mappedBy = "matchList")
    private List<Team> teams;


    public Game() {
        statsSaved = false;
        gameResult = GameResult.create();
        teams = new LinkedList<>();
    }

    public Game(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        teams = new LinkedList<>();
        statsSaved = false;
        gameResult = GameResult.create();
    }

    public Game(String homeTeam, String awayTeam, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        teams = new LinkedList<>();
        gameResult = GameResult.create();
        statsSaved = false;
    }

    public Game(String homeTeam, String awayTeam, int matchNumber, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchNumber = matchNumber;
        this.round = round;
        gameResult = GameResult.create();
        teams = new LinkedList<>();
        statsSaved = false;
    }

    @Override
    public int compareTo(Game game) {
        return Integer.compare(this.getMatchNumber(), game.getMatchNumber());
    }
}
