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
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    private String scoutPath;

    //Match data
    private String matchDate;
    private String matchResult;
    private String set1Result;
    private String set2Result;
    private String set3Result;
    private String set4Result;
    private String set5Result;

    @ManyToOne
    Round round;

    @ManyToMany(mappedBy = "matchList")
    private List<Team> teams;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        teams = new LinkedList<>();
    }

    public Match(String homeTeam, String awayTeam, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        teams = new LinkedList<>();
    }

    public Match(String homeTeam, String awayTeam, int matchNumber, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchNumber = matchNumber;
        this.round = round;
        teams = new LinkedList<>();
    }
}
