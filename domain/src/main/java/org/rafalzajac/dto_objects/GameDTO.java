package org.rafalzajac.dto_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rafalzajac.domain.Round;


@Data
@NoArgsConstructor
public class GameDTO {

    private Long id;
    private String homeTeam;
    private String awayTeam;
    private int matchNumber;
    Round round;

}
