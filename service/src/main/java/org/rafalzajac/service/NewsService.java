package org.rafalzajac.service;

import org.rafalzajac.domain.News;
import org.rafalzajac.repository.NewsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void saveNewsToDatabase(News news) {
        newsRepository.save(news);
    }

    public Optional<News> findNewsById (Long id) {
        return newsRepository.findById(id);
    }

    public List<News> findAllNews() {
        return newsRepository.findAll();
    }

    public void deleteArticleById(Long id) {
        newsRepository.deleteById(id);
    }
}
