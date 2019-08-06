package org.rafalzajac.domain;


import com.google.common.base.Preconditions;
import lombok.Getter;
import javax.persistence.*;


@Entity
@Getter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int number;
    private int age;
    private int height;
    private String playerTag;
    private String firstName;
    private String lastName;
    private String position;


    @ManyToOne
    private Team team;

    private PlayerStats playerStats;

    public Player() {
        this.playerStats = new PlayerStats();
    }


    public Player(int number, String playerTag, String firstName, String lastName, Team team) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.playerStats = new PlayerStats();
    }

    public void setNumber(int number) {

        Preconditions.checkArgument((number > 0) && (number <= 100), "Player number cannot be 0, negative, or greater than 100");

        this.number = number;
    }

    public void setAge(int age) {

        Preconditions.checkArgument( (age > 0) && (age <= 100), "Age cannot be negative, zero or grater than 100");

        this.age = age;
    }

    public void setHeight(int height) {

        Preconditions.checkArgument( (height >= 100) && (height <= 250), "Height must be positive number higher or equal 100 and lesser or equal to 250cm");

        this.height = height;

    }

    public void setFirstName(String firstName){

        Preconditions.checkNotNull(firstName, "Name cannot be null");
        Preconditions.checkArgument(!firstName.trim().equals(""), "Name cannot be empty");

        this.firstName = firstName;
    }

    public void setLastName(String lastName){

        Preconditions.checkNotNull(lastName, "Last name cannot be null");
        Preconditions.checkArgument(!lastName.trim().equals(""), "Last name cannot be empty");

        this.lastName = lastName;
    }

    public void setPosition(String position) {

        Preconditions.checkNotNull(position, "Position cannot be null");
        Preconditions.checkArgument(!position.trim().equals(""), "Position cannot be empty");

        this.position = position;
    }

    public void setTeam(Team team) {

        Preconditions.checkNotNull(team, "Team cannot be null");

        this.team = team;
    }

    public void setPlayerStats(PlayerStats playerStats) {

        Preconditions.checkNotNull(playerStats, "Player stats cannot be null");

        this.playerStats = playerStats;
    }
}
