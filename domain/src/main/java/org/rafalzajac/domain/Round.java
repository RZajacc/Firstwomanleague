package org.rafalzajac.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Round {

    @Id
    @GeneratedValue
    private Long id;

    private int roundNumber;

    @OneToMany(mappedBy = "round")
    private List<Match> matchList;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    @ManyToOne
    League league;

}
