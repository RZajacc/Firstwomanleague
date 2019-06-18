package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


}
