package org.rafalzajac.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class News {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String shortDescription;
    private String content;
    private String dateOfCreation;


    public News() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateOfCreation = now.format(formatter);
    }

    public void setContent(String content) {

        if (content.length() > 100){
            shortDescription = content.substring(0,100);
            shortDescription += "...";
        } else {
            shortDescription = content;
        }

        this.content = content;
    }
}
