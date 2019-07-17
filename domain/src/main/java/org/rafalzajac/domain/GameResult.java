package org.rafalzajac.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Data
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private int homeTeamSetsWon;
    private int awayTeamSetsWon;

    //Set result for each team
    private int homeTeamSet1Score;
    private int homeTeamSet2Score;
    private int homeTeamSet3Score;
    private int homeTeamSet4Score;
    private int homeTeamSet5Score;

    private int awayTeamSet1Score;
    private int awayTeamSet2Score;
    private int awayTeamSet3Score;
    private int awayTeamSet4Score;
    private int awayTeamSet5Score;


    @OneToOne
    private Game game;
}
