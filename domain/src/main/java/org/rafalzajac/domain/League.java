package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class League {

    @Id
    @GeneratedValue
    private Long id;

    private String leagueName;
    private String season;

    @OneToMany(mappedBy = "league")
    private List<Round> roundList;

    public League(String leagueName, String season) {
        this.leagueName = leagueName;
        this.season = season;
        roundList = new LinkedList<>();
    }

    public void addRound(Round round) {
        roundList.add(round);
    }

}
