package org.rafalzajac.dto_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rafalzajac.domain.Team;

@Data
@NoArgsConstructor
public class PlayerDTO {

    private Long id;
    private int number;
    private String playerTag;
    private String firstName;
    private String lastName;
    private String position;
    private int age;
    private int height;

    private Team team;
}
