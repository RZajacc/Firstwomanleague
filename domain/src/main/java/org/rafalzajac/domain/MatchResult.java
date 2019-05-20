package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@Getter @Setter
public class MatchResult {

    @Id @GeneratedValue
    private Long id;

    private String homeTeamSetsWon;
    private String awayTeamSetsWon;

    //Set result for each team
    private String homeTeamSet1Score;
    private String homeTeamSet2Score;
    private String homeTeamSet3Score;
    private String homeTeamSet4Score;
    private String homeTeamSet5Score;

    private String awayTeamSet1Score;
    private String awayTeamSet2Score;
    private String awayTeamSet3Score;
    private String awayTeamSet4Score;
    private String awayTeamSet5Score;


    @OneToOne
    private Match match;
}
