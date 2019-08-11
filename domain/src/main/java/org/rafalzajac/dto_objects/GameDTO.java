package org.rafalzajac.dto_objects;

import lombok.Data;
import org.rafalzajac.domain.GameResult;
import org.rafalzajac.domain.Round;
import org.rafalzajac.domain.Team;
import java.util.LinkedList;
import java.util.List;


@Data
public class GameDTO {

    private Long id;
    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    private Round round;

    private GameResult gameResult;
    private List<Team> teams;

    public GameDTO() {
        this.gameResult = GameResult.create();
        this.teams = new LinkedList<>();
    }
}
