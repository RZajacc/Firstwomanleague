package org.rafalzajac.dto_objects;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDTO {

    private Long id;

    private String teamTag;
    private String teamName;
    private String firstCoach;
    private String secondCoach;
    private String webPage;
    private String facebook;


}
