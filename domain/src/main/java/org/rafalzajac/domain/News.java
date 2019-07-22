package org.rafalzajac.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String shortDescription;

    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String createdAt;
    private LocalDateTime creationDate;


    public News() {
        creationDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdAt = creationDate.format(formatter);
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
