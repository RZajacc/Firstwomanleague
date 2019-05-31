package org.rafalzajac.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private int number;
    private String playerTag;
    private String firstName;
    private String lastName;
    private String position;
    private int age;
    private int height;

    @ManyToOne
    private Team team;

    @OneToOne
    private PlayerStats playerStats;

    public Player(int number, String playerTag, String firstName, String lastName) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(int number, String playerTag, String firstName, String lastName, Team team) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
    }

    public Player(int number, String playerTag, String firstName, String lastName, Team team, PlayerStats playerStats) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.playerStats = playerStats;
    }
}
