package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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

    /**
     * No args constructor. It gets instance of PlayerStats during creation
     */
    private Player() {
        this.playerStats = PlayerStats.create();
    }

    /**
     * Constructor used for data obtained from DataVolley scouting file. It also gets instance of PLayerStats
     * @param number - Player jersey number
     * @param playerTag - It's created by DataVolley with three first letters of first and last name like "Raf-Zaj"
     * @param firstName - Name read from file
     * @param lastName - Last name read from file
     * @param team - Team is also assigned from scouting file
     */
    private Player(int number, String playerTag, String firstName, String lastName, Team team) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.playerStats = PlayerStats.create();
    }

    /**
     * Player number must be greater than 0 and smaller than 100. To check conditions I used Google Guava library.
     * @param number - Jersey number
     */
    public void setNumber(int number) {

        Preconditions.checkArgument((number > 0) && (number < 100), "Player number cannot be 0, negative, or greater than 100");

        this.number = number;
    }

    /**
     * Age (in years) must be positive number smaller than 100. To check conditions I used Google Guava library.
     * @param age
     */
    public void setAge(int age) {

        Preconditions.checkArgument( (age > 0) && (age < 100), "Age cannot be negative, zero or grater than 100");

        this.age = age;
    }

    /**
     * Height in centimeteres. Value should be greater or equal to 100 and smaller or equal to 250. To check conditions I used Google Guava library.
     * @param height
     */
    public void setHeight(int height) {

        Preconditions.checkArgument( (height >= 100) && (height <= 250), "Height must be positive number higher or equal 100 and lesser or equal to 250cm");

        this.height = height;

    }

    /**
     * First name cannot be set to null, and it does not accept empty string as input. To check conditions I used Google Guava library.
     * @param firstName
     */
    public void setFirstName(String firstName){

        Preconditions.checkArgument(!Strings.isNullOrEmpty(firstName), "First name cannot be null or empty");

        this.firstName = firstName;
    }

    /**
     * Last name cannot be set to null, and it does not accept empty string as input. To check conditions I used Google Guava library.
     * @param lastName
     */
    public void setLastName(String lastName){

        Preconditions.checkArgument(!Strings.isNullOrEmpty(lastName), "Last name cannot be null or empty");

        this.lastName = lastName;
    }

    /**
     * Position on court cannot be set to null, and it does not accept empty string as input.
     * To check conditions I used Google Guava library.
     * @param position
     */
    public void setPosition(String position) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(position), "Position cannot be null or empty");

        this.position = position;
    }

    /**
     * Team cannot have null reference. To check conditions I used Google Guava library.
     * @param team
     */
    public void setTeam(Team team) {

        Preconditions.checkNotNull(team, "Team cannot be null");

        this.team = team;
    }

    /**
     * Player stats cannot get null reference. To check conditions I used Google Guava library.
     * @param playerStats
     */
    public void setPlayerStats(PlayerStats playerStats) {

        Preconditions.checkNotNull(playerStats, "Player stats cannot be null");

        this.playerStats = playerStats;
    }

    /**
     * Factory method for no argument constructor
     * @return Player instance with default values and instantiated PlayerStats
     */
    public static Player createEmpty () {
        return new Player();
    }

    /**
     * Factory method for creating Player object with data obtained from DataVolley scout file
     * @param number - Jersey number
     * @param playerTag - Tag creeated from first and last name
     * @param firstName - First name from DV file
     * @param lastName - Last name from DV file
     * @param team - Team assigned from DV file
     * @return
     */
    public static Player createForDvDataFile(int number, String playerTag, String firstName, String lastName, Team team) {
        return new Player(number, playerTag, firstName, lastName, team);
    }


}
