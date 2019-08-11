package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import lombok.*;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
public class Round implements Comparable<Round>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int roundNumber;

    @ManyToOne
    League league;

    @OneToMany(mappedBy = "round")
    private List<Game> matchList;

    /**
     * No argument constructor with list initialization
     */
    private Round () {
        matchList = new LinkedList<>();
    }

    /**
     * Constructor used for creating all rounds in the league. It is used mostly in DataBaseLoader class
     * @param roundNumber -  integer with round number you want to add
     * @param league - league that it will be attributed to. For now there is only one but it might be usefull later
     */
    private Round(int roundNumber, League league) {
        this.roundNumber = roundNumber;
        this.league = league;
        matchList = new LinkedList<>();

    }

    /**
     * Setter method for round number
     * @param roundNumber - it should be number higher than zero and smaller or equal to 50
     */
    public void setRoundNumber(int roundNumber) {
        Preconditions.checkArgument(roundNumber>0 && roundNumber <= 50, "Number must be higher than 0 and smaller or equal to 50");
        this.roundNumber = roundNumber;
    }

    /**
     * Setter method for league
     * @param league - pass league object or just change values in existing one to modify data.
     */
    public void setLeague(League league) {
        Preconditions.checkNotNull(league);
        this.league = league;
    }

    /**
     * Setter method for match list. For most of the time setter method is not necessary. It is best to add elements to the list
     * @param matchList - Array list of game objects
     */
    public void setMatchList(List<Game> matchList) {
        Preconditions.checkNotNull(matchList);
        this.matchList = matchList;
    }

    /**
     * Factory method for no argument constructor
     * @return - round instance with default values
     */
    public static Round createNoArgs() {
        return new Round();
    }

    /**
     * Factory method for all argument constructor.
     * @param roundNumber - round number you wish to add
     * @param league - league that you want to assign to.
     * @return - Round instance with all arguments.
     */
    public static Round createWithAllArguments(int roundNumber, League league) {
        return new Round(roundNumber, league);
    }


    @Override
    public int compareTo(Round round) {
        return Integer.compare(this.getRoundNumber(), round.getRoundNumber());
    }
}
