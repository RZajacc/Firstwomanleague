package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Round implements Comparable<Round>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int roundNumber;

    @ManyToOne
    League league;

    @OneToMany(mappedBy = "round")
    private List<Game> matchList;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        matchList = new LinkedList<>();
    }

    public Round(int roundNumber, League league) {
        this.roundNumber = roundNumber;
        this.league = league;
        matchList = new LinkedList<>();

    }

    @Override
    public int compareTo(Round round) {
        return Integer.compare(this.getRoundNumber(), round.getRoundNumber());
    }
}
