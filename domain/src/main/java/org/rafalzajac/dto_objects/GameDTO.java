package org.rafalzajac.dto_objects;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import org.rafalzajac.domain.GameResult;
import org.rafalzajac.domain.Round;
import org.rafalzajac.domain.Team;
import java.util.LinkedList;
import java.util.List;


@Getter
public class GameDTO {

    private Long id;
    private String homeTeam;
    private String awayTeam;
    private int matchNumber;

    private Round round;
    private GameResult gameResult;
    private List<Team> teams;

    /**
     * No arguments constructor initiating gameResult and list of teams
     */
    private GameDTO() {
        this.gameResult = GameResult.create();
        this.teams = new LinkedList<>();
    }


    public void setHomeTeam(String homeTeam) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(homeTeam));
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(awayTeam));
        this.awayTeam = awayTeam;
    }

    public void setMatchNumber(int matchNumber) {
        Preconditions.checkArgument(matchNumber > 0 && matchNumber <= 300, "Match number must be higher than 0 and smaller or equal to 300");
        this.matchNumber = matchNumber;
    }

    public void setRound(Round round) {
        Preconditions.checkNotNull(round);
        this.round = round;
    }

    public void setGameResult(GameResult gameResult) {
        Preconditions.checkNotNull(gameResult);
        this.gameResult = gameResult;
    }

    public void setTeams(List<Team> teams) {
        Preconditions.checkNotNull(teams);
        this.teams = teams;
    }

    /**
     * Factory method for creating GameDTO object
     * @return - GameDTO instance with default values
     */
    public static GameDTO create() {
        return new GameDTO();
    }
}
