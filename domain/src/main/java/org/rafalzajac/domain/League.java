package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Getter
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String leagueName;
    private String season;

    @OneToMany(mappedBy = "league")
    private List<Round> roundList;

    /**
     * No argument construtor used for factory method
     */
    private League() {
        roundList = new LinkedList<>();
    }

    /**
     * All argument constructor used for factory method
     * @param leagueName - string value. Cannot be empty or null
     * @param season - string value. Cannot be empty or null
     */
    private League(String leagueName, String season) {
        this.leagueName = leagueName;
        this.season = season;
        roundList = new LinkedList<>();
    }

    /**
     * Setter method for league name. Cannot be null or empty
     * @param leagueName
     */
    public void setLeagueName(String leagueName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(leagueName));
        this.leagueName = leagueName;
    }

    /**
     * Setter method for season name. Cannot be null or empty
     * @param season
     */
    public void setSeason(String season) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(season));
        this.season = season;
    }

    /**
     * Setter method for round list. You can add new list, but for most of the time its better to add elements to existing one
     * @param roundList
     */
    public void setRoundList(List<Round> roundList) {
        Preconditions.checkNotNull(roundList);
        this.roundList = roundList;
    }

    /**
     * Factory method for no argument constructor
     * @return league instance with default values
     */
    public static League createNoArgs() {
        return new League();
    }

    /**
     * Factory method for all arguments constructor
     * @param leagueName - string value for league name
     * @param season - string value for league name
     * @return
     */
    public static League createAllArgs(String leagueName, String season) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(leagueName));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(season));

        return new League(leagueName, season);
    }

}
