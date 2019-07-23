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


    @ManyToOne
    Round round;

    @ManyToMany(mappedBy = "matchList")
    private List<Team> teams;


    private GameResult gameResult;


    public Game() {
        this.statsSaved = false;
        this.gameResult = new GameResult();
        this.teams = new LinkedList<>();
    }

    public Game(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.teams = new LinkedList<>();
        this.statsSaved = false;
        this.gameResult = new GameResult();
    }

    public Game(String homeTeam, String awayTeam, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        this.teams = new LinkedList<>();
        this.gameResult = new GameResult();
        this.statsSaved = false;
    }

    public Game(String homeTeam, String awayTeam, int matchNumber, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchNumber = matchNumber;
        this.round = round;
        this.gameResult = new GameResult();
        this.teams = new LinkedList<>();
        this.statsSaved = false;
    }

    @Override
    public int compareTo(Game game) {
        return Integer.compare(this.getMatchNumber(), game.getMatchNumber());
    }
}
