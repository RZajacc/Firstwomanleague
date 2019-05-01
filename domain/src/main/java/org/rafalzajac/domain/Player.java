package org.rafalzajac.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private int number;
    private String playerTag;
    private String firstName;
    private String lastName;
    private String position;

    @ManyToOne
    private Team team;


    public Player(int number, String playerTag, String firstName, String lastName) {
        this.number = number;
        this.playerTag = playerTag;
        this.firstName = firstName;
        this.lastName = lastName;
    }


}
