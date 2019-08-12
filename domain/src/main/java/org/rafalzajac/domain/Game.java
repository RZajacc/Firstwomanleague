package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
public class Game implements Comparable<Game>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    private String scoutPath;
    private Boolean statsSaved;

    private GameResult gameResult;

    @ManyToOne
    private Round round;

    @ManyToMany(mappedBy = "matchList")
    private List<Team> teams;

    /**
     * No arguments construcotr with initialization of necessary fields.
     */
    private Game() {
        statsSaved = false;
        gameResult = GameResult.create();
        teams = new LinkedList<>();
    }

    /**
     * Setter methods for team names
     * @param homeTeam - string value. Cannot be null or empty
     */
    public void setHomeTeam(String homeTeam) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(homeTeam));
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(awayTeam));
        this.awayTeam = awayTeam;
    }

    /**
     * Setter method for match number
     * @param matchNumber - it can vary between 1 - 300
     */
    public void setMatchNumber(int matchNumber) {
        Preconditions.checkArgument(matchNumber > 0 && matchNumber <= 300);
        this.matchNumber = matchNumber;
    }

    /**
     * Setter method for scout Path. It is assigned to AmazonS3 storage.
     * @param scoutPath - value cannot be empty string or null
     */
    public void setScoutPath(String scoutPath) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(scoutPath));
        this.scoutPath = scoutPath;
    }

    /**
     * Setter method for StatsSaved. It stores boolean information if data were saved to database or not.
     * Default is false. Value is changed to true after scout file processing and choosing to save data.
     * @param statsSaved
     */
    public void setStatsSaved(Boolean statsSaved) {
        this.statsSaved = statsSaved;
    }

    /**
     * Setter method for GameResult class representing matchScore.
     * @param gameResult - cannot be null reference
     */
    public void setGameResult(GameResult gameResult) {
        Preconditions.checkNotNull(gameResult);
        this.gameResult = gameResult;
    }

    /**
     * Setter method for round.
     * @param round
     */
    public void setRound(Round round) {
        Preconditions.checkNotNull(round);
        this.round = round;
    }

    /**
     * Setter method for teams participating in the game
     * @param teams - list of type team.
     */
    public void setTeams(List<Team> teams) {
        Preconditions.checkNotNull(teams);
        this.teams = teams;
    }

    /**
     * Factory method for creating game instance
     * @return - instance of Game object with default values.
     */
    public static Game create() {
        return new Game();
    }

    @Override
    public int compareTo(Game game) {
        return Integer.compare(this.getMatchNumber(), game.getMatchNumber());
    }
}
