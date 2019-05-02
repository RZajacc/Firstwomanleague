package org.rafalzajac.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter
public class Round {

    @Id
    @GeneratedValue
    private Long id;

    private int roundNumber;

    @ManyToOne
    League league;

    @OneToMany(mappedBy = "round")
    private List<Match> matchList;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        matchList = new LinkedList<>();
    }

    public Round(int roundNumber, League league) {
        this.roundNumber = roundNumber;
        this.league = league;
        matchList = new LinkedList<>();

    }




    public void addMatch(Match match){
        matchList.add(match);
    }



}
