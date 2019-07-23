package org.rafalzajac.domain;

import lombok.Data;
import javax.persistence.*;


@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    private PlayerStats playerStats;

    public Player() {
        this.playerStats = new PlayerStats();
    }

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
        this.playerStats = new PlayerStats();
    }

}
