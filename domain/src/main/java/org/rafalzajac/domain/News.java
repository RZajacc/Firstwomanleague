package org.rafalzajac.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
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


    /**
     * No argument constructor used for factory method and also gets current time as creation date, and it is also
     * saved as formatted string in createdAt variable
     */
    private News() {
        creationDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdAt = creationDate.format(formatter);
    }

    /**
     * Setter method for title.
     * @param title - string value. Cannot be empty or null
     */
    public void setTitle(String title) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(title));
        this.title = title;
    }

    /**
     * Setter method for content. It checks input length. It cannot be null or empty and if it is shorter or equal to
     * 100 characters then short description is the same as content. If content is longer then short description is set
     * as first 100 chars of the content and "..." is added at the end.
     * @param content - not empty, not null string value
     */
    public void setContent(String content) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(content));

        if (content.length() > 100){
            shortDescription = content.substring(0,100);
            shortDescription += "...";
        } else {
            shortDescription = content;
        }

        this.content = content;
    }

    /**
     * Factory method for creating News object
     * @return - News instance with default values
     */
    public static News create() {
        return new News();
    }
}
