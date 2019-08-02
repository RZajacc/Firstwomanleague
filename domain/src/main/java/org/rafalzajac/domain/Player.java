package org.rafalzajac.domain;

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

    // This constructor is not included in testing since it is used only to create temporary data from data volley file
    // In scoutFileProcess class. Players created with this constructor are not stored in database or used in any other places.
    // It is impossible to create player without number or with negative one in DV. But there is a chance to prove empty
    // values (but not giving null) for other values (name, last name) so for this case I'm not restricting constructor for this cases. It would
    // crash the program for valid Data Volley data -> for example player with number and name but no other data. For this purpose
    // empty values are fine.
    public Player(int number, String playerTag, String firstName, String lastName, Team team) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.playerStats = new PlayerStats();
    }

    public void setNumber(int number) {

        if ( (number <= 0) || (number > 100) ) {
            throw new IllegalArgumentException("Player number cannot be 0, negative, or greater than 100");
        }

        this.number = number;
    }

    public void setAge(int age) {

        if ( (age <= 0) || (age > 100) ) {
            throw new IllegalArgumentException("Age must be a positive number smaller than 100");
        }

        this.age = age;
    }

    public void setHeight(int height) {

        if( (height < 100) || (height > 250)) {
            throw new IllegalArgumentException("Height must be positive number higher than 100 and lesser than 250cm");
        }

        this.height = height;

    }

    public void setFirstName(String firstName){

        if (firstName == null) {
            throw new NullPointerException("Name cannot have null value");
        }

        if (firstName.trim().equals("")) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.firstName = firstName;
    }

    public void setLastName(String lastName){

        if (lastName == null) {
            throw new NullPointerException("Last name cannot have null value");
        }

        if (lastName.trim().equals("")) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        this.lastName = lastName;
    }

    public void setPosition(String position) {

        if (position == null) {
            throw new NullPointerException("Position cannot have null value");
        }

        if (position.trim().equals("")) {
            throw new IllegalArgumentException("Position cannot be empty");
        }

        this.position = position;
    }

    public void setTeam(Team team) {

        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }

        this.team = team;
    }

    public void setPlayerStats(PlayerStats playerStats) {

        if (playerStats == null) {
            throw new IllegalArgumentException("Player stats cannot be null");
        }

        this.playerStats = playerStats;
    }
}
