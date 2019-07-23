package org.rafalzajac.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
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
