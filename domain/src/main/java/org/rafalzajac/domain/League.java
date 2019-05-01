package org.rafalzajac.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class League {

    @Id
    @GeneratedValue
    private Long id;

    private String leagueName;
    private String season;

    @OneToMany(mappedBy = "league")
    private List<Round> roundList;
}
