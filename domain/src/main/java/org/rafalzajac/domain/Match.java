package org.rafalzajac.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue
    private Long id;

//    private String homeTeam;
//    private String awayTeam;
    private String matchDate;
    private String matchResult;

    @OneToMany(mappedBy = "match")
    private List<Team> teams;


    @ManyToOne
    Round round;

}
