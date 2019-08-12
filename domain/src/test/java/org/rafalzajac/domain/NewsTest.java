package org.rafalzajac.domain;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class NewsTest {

    private News news;

    @Before
    public void before() {
        news = News.create();
    }

    @Test
    public void createTest() {
        assertThat(news.getId()).isNull();
        assertThat(news.getTitle()).isNull();
        assertThat(news.getShortDescription()).isNull();
        assertThat(news.getContent()).isNull();
        assertThat(news.getCreatedAt()).isNotEmpty();
        assertThat(news.getCreationDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
    }

    @Test
    public void setValidTitle() {
        news.setContent("Valid content");
        assertThat(news.getContent()).isEqualTo("Valid content");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEmptyContent() {
        news.setContent("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullContent() {
        news.setContent(null);
    }

    @Test
    public void checkShortDescriptionShorterThan100Chars() {
        news.setContent("Short content");
        assertThat(news.getShortDescription().length()).isEqualTo(13);
    }

    @Test
    public void checkShortDescriptionLongerThan100Chars() {
        news.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua");
        assertThat(news.getShortDescription().length()).isEqualTo(103);
    }
}
