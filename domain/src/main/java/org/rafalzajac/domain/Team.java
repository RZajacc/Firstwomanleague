package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String teamTag;
    private String teamName;
    private String firstCoach;
    private String secondCoach;
    private String webPage;
    private String facebook;

    @ManyToMany
    private List<Game> matchList;

    @OneToMany(mappedBy = "team")
    private List<Player> playerList;

    private TeamStats teamStats;

    /**
     * No arguments constructor initiating playerList, matchList and teamStats;
     */
    private Team () {
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = TeamStats.create();
    }

    /**
     * Constructor used in creating teams from Data Volley file. At least one coach is always provided but second one
     * is optional.
     * @param teamTag - created from three first letters of team capitalised. Value read from file.
     * @param teamName - team name read from Data Volley file
     * @param firstCoach - first coach name value read from Data Volley file.
     */
    private Team(String teamTag, String teamName, String firstCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = TeamStats.create();
    }

    /**
     * Constructor used in creating teams from Data Volley file. At least one coach is always provided but second one
     * is optional.
     * @param teamTag - created from three first letters of team capitalised. Value read from file.
     * @param teamName - team name read from Data Volley file.
     * @param firstCoach - first coach name value read from Data Volley file.
     * @param secondCoach - second coach name value read from Data Volley file.
     */
    private Team(String teamTag, String teamName, String firstCoach, String secondCoach) {
        this.teamTag = teamTag;
        this.teamName = teamName;
        this.firstCoach = firstCoach;
        this.secondCoach = secondCoach;
        playerList = new LinkedList<>();
        matchList = new LinkedList<>();
        teamStats = TeamStats.create();
    }

    /**
     * Methdo adding player to the team.
     * @param player - player to be assigned to team.
     */
    public void addPlayer (Player player) {
        playerList.add(player);
    }

    /**
     * Facotry method for creating team with no arguments constructor
     * @return - team instatnce with default values.
     */
    public static Team createNoArgs() {
        return new Team();
    }

    /**
     * Facotry method for creating team with all data that are always present in Data Volley file those are listed below
     * @param teamTag - created from three first letters of team capitalised. Value read from file.
     * @param teamName - team name read from Data Volley file.
     * @param firstCoach - first coach name value read from Data Volley file.
     * @return
     */
    public static Team createFirstCoachOnly(String teamTag, String teamName, String firstCoach) {
        return new Team(teamTag, teamName, firstCoach);
    }

    /**
     * Facotry method for creating team with all data that are always present in Data Volley file with second coach
     * @param teamTag - created from three first letters of team capitalised. Value read from file.
     * @param teamName - team name read from Data Volley file.
     * @param firstCoach - first coach name value read from Data Volley file.
     * @param secondCoach - second coach name value read from Data Volley file.
     * @return
     */
    public static Team createBothCoaches(String teamTag, String teamName, String firstCoach, String secondCoach) {
        return new Team(teamTag, teamName, firstCoach, secondCoach);
    }

    /**
     * Setter method for team tag. It cannot be null or empty string
     * @param teamTag - you should choose the one used in Data Volley files in order for database to work
     */
    public void setTeamTag(String teamTag) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(teamTag));
        this.teamTag = teamTag;
    }

    /**
     * Setter method for team name. It can be anything but empty string or null value
     * @param teamName
     */
    public void setTeamName(String teamName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(teamName));
        this.teamName = teamName;
    }

    /**
     * Setter method for first coach name. It can be anything but empty string or null value
     * @param firstCoach
     */
    public void setFirstCoach(String firstCoach) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(firstCoach));
        this.firstCoach = firstCoach;
    }

    /**
     * Setter method for team name. It can be anything but empty string or null value
     * @param secondCoach
     */
    public void setSecondCoach(String secondCoach) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(secondCoach));
        this.secondCoach = secondCoach;
    }

    /**
     * Setter method for facebook adress. It can be anything but empty string or null value. Validity of adress is not verified
     * @param facebook
     */
    public void setFacebook(String facebook) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(facebook));
        this.facebook = facebook;
    }

    /**
     * Setter method for web page adress. It can be anything but empty string or null value. Validity of adress is not verified
     * @param webPage
     */
    public void setWebPage(String webPage) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(webPage));
        this.webPage = webPage;
    }

}
