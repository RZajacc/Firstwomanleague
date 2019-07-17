package org.rafalzajac.dto_objects;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class NewsDTO {

    private String title;
    private String shortDescription;
    private String content;
    private String createdAt;
    private LocalDateTime creationDate;


    public NewsDTO() {
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
