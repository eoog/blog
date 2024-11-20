package com.www.back.task;


import com.www.back.entity.Article;
import com.www.back.entity.HotArticle;
import com.www.back.repository.ArticleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyHotArticleTasks {

  private static final String REDIS_KEY = "hot-article:";

  private final ArticleRepository articleRepository;

  private RedisTemplate<String, Object> redisTemplate;

  public DailyHotArticleTasks(ArticleRepository articleRepository,
      RedisTemplate<String, Object> redisTemplate) {
    this.articleRepository = articleRepository;
    this.redisTemplate = redisTemplate;
  }


  @Scheduled(cron = "15 23 07 * * ?")
  public void pickYesterdayHotArticle() {
    LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
    LocalDateTime endDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    Article article = articleRepository.findHotArticle(startDate, endDate);
    if (article == null) {
      return;
    }
    HotArticle hotArticle = new HotArticle();
    hotArticle.setId(article.getId());
    hotArticle.setTitle(article.getTitle());
    hotArticle.setContent(article.getContent());
    hotArticle.setAuthorName(article.getAuthor().getUsername());
    hotArticle.setCreatedDate(article.getCreatedDate());
    hotArticle.setUpdatedDate(article.getUpdatedDate());
    hotArticle.setViewCount(article.getViewCount());
    redisTemplate.opsForHash().put(REDIS_KEY + article.getId(), article.getId(), hotArticle);
  }
}
