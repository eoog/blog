package com.www.back.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.back.dto.WriteCommentDto;
import com.www.back.entity.Article;
import com.www.back.entity.Board;
import com.www.back.entity.Comment;
import com.www.back.entity.HotArticle;
import com.www.back.entity.User;
import com.www.back.exception.ForbiddenException;
import com.www.back.exception.RateLimitException;
import com.www.back.exception.ResourceNotFoundException;
import com.www.back.pojo.WriteComment;
import com.www.back.repository.ArticleRepository;
import com.www.back.repository.BoardRepository;
import com.www.back.repository.CommentRepository;
import com.www.back.repository.UserRepository;
import com.www.back.task.DailyHotArticleTasks;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  private final BoardRepository boardRepository;
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ElasticSearchService elasticSearchService;
  private final ObjectMapper objectMapper;
  private final RabbitMQSender rabbitMQSender;

  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public CommentService(BoardRepository boardRepository, ArticleRepository articleRepository,
      CommentRepository commentRepository, UserRepository userRepository, ObjectMapper objectMapper,
      ElasticSearchService elasticSearchService, RabbitMQSender rabbitMQSender,
      RedisTemplate<String, Object> redisTemplate) {
    this.boardRepository = boardRepository;
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.elasticSearchService = elasticSearchService;
    this.objectMapper = objectMapper;
    this.rabbitMQSender = rabbitMQSender;
    this.redisTemplate = redisTemplate;
  }

  // 댓글 작성
  @Transactional
  public Comment writeComment(Long boardId, Long articleId, WriteCommentDto dto) {
    // 사용자 계정 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    if (!this.isCanWriteComment()) {
      throw new RateLimitException("comment not written by rate limit");
    }

    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());
    Optional<Board> board = boardRepository.findById(boardId);
    Optional<Article> article = articleRepository.findById(articleId);
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    if (article.get().getIsDeleted()) {
      throw new ForbiddenException("article is deleted");
    }

    Comment comment = new Comment();
    comment.setArticle(article.get());
    comment.setAuthor(author.get());
    comment.setContent(dto.getContent());
    commentRepository.save(comment);

    // 댓글 작성 알림 RabbitMQ
    WriteComment writeComment = new WriteComment();
    writeComment.setCommentId(comment.getId());
    rabbitMQSender.send(writeComment);
    return comment;

  }

  // 댓글 수정
  @Transactional
  public Comment editComment(Long boardId, Long articleId, Long commentId, WriteCommentDto dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    if (!this.isCanEditComment()) {
      throw new RateLimitException("comment not written by rate limit");
    }
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());
    Optional<Board> board = boardRepository.findById(boardId);
    Optional<Article> article = articleRepository.findById(articleId);
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    if (article.get().getIsDeleted()) {
      throw new ForbiddenException("article is deleted");
    }
    Optional<Comment> comment = commentRepository.findById(commentId);
    if (comment.isEmpty() || comment.get().getIsDeleted()) {
      throw new ResourceNotFoundException("comment not found");
    }
    if (comment.get().getAuthor() != author.get()) {
      throw new ForbiddenException("comment author different");
    }
    if (dto.getContent() != null) {
      comment.get().setContent(dto.getContent());
    }
    commentRepository.save(comment.get());
    return comment.get();
  }

  // 댓글 삭제
  public boolean deleteComment(Long boardId, Long articleId, Long commentId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    if (!this.isCanEditComment()) {
      throw new RateLimitException("comment not written by rate limit");
    }
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());
    Optional<Board> board = boardRepository.findById(boardId);
    Optional<Article> article = articleRepository.findById(articleId);
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    if (article.get().getIsDeleted()) {
      throw new ForbiddenException("article is deleted");
    }
    Optional<Comment> comment = commentRepository.findById(commentId);
    if (comment.isEmpty() || comment.get().getIsDeleted()) {
      throw new ResourceNotFoundException("comment not found");
    }
    if (comment.get().getAuthor() != author.get()) {
      throw new ForbiddenException("comment author different");
    }
    comment.get().setIsDeleted(true);
    commentRepository.save(comment.get());
    return true;
  }

  // 작성 확인
  private boolean isCanWriteComment() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Comment latestComment = commentRepository.findLatestCommentOrderByCreatedDate(
        userDetails.getUsername());
    if (latestComment == null) {
      return true;
    }
    return this.isDifferenceMoreThanOneMinutes(latestComment.getCreatedDate());
  }

  private boolean isCanEditComment() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Comment latestComment = commentRepository.findLatestCommentOrderByCreatedDate(
        userDetails.getUsername());
    if (latestComment == null || latestComment.getUpdatedDate() == null) {
      return true;
    }
    return this.isDifferenceMoreThanOneMinutes(latestComment.getUpdatedDate());
  }

  // 댓글 단지 1분이내 확인
  private boolean isDifferenceMoreThanOneMinutes(LocalDateTime localDateTime) {
    LocalDateTime dateAsLocalDateTime = new Date().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    Duration duration = Duration.between(localDateTime, dateAsLocalDateTime);

    return Math.abs(duration.toMinutes()) > 1;
  }

  // 기사 조회
  @Async
  @Transactional
  protected CompletableFuture<Article> getArticle(Long boardId, Long articleId)
      throws JsonProcessingException {

    // 어제 인기글 이번주 인기글
    Object yesterdayHotArticleTempObj = redisTemplate.opsForHash().get(
        DailyHotArticleTasks.YESTERDAY_REDIS_KEY + articleId, articleId);
    Object weekHotArticleTempObj = redisTemplate.opsForHash()
        .get(DailyHotArticleTasks.WEEK_REDIS_KEY + articleId, articleId);
    if (yesterdayHotArticleTempObj != null || weekHotArticleTempObj != null) {
      HotArticle hotArticle = (HotArticle) (yesterdayHotArticleTempObj != null
          ? yesterdayHotArticleTempObj : weekHotArticleTempObj);
      Article article = new Article();
      article.setId(hotArticle.getId());
      article.setTitle(hotArticle.getTitle());
      article.setContent(hotArticle.getContent());
      User user = new User();
      user.setUsername(hotArticle.getAuthorName());
      article.setAuthor(user);
      article.setCreatedDate(hotArticle.getCreatedDate());
      article.setUpdatedDate(hotArticle.getUpdatedDate());
      article.setViewCount(hotArticle.getViewCount());
      return CompletableFuture.completedFuture(article);
    }

    // redis에 없으면 mysql에서 조회
    // 게시판 조회
    Optional<Board> board = boardRepository.findById(boardId);
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }
    // 기사 조회
    Optional<Article> article = articleRepository.findById(articleId);
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    // 일라스틱 서치에 등록
    article.get().setViewCount(article.get().getViewCount() + 1);
    articleRepository.save(article.get());
    String articleJson = objectMapper.writeValueAsString(article.get());
    elasticSearchService.indexArticleDocument(article.get().getId().toString(), articleJson)
        .block();
    return CompletableFuture.completedFuture(article.get());
  }

  // 기사의 모든 댓글
  @Async
  protected CompletableFuture<List<Comment>> getComments(Long articleId) {
    return CompletableFuture.completedFuture(commentRepository.findByArticleId(articleId));
  }

  // 게시판 및 댓글 모두 가져오기
  public CompletableFuture<Article> getArticleWithComments(Long boardId, Long articleId)
      throws JsonProcessingException {
    CompletableFuture<Article> articleCompletableFuture = this.getArticle(boardId, articleId);
    CompletableFuture<List<Comment>> commentsCompletableFuture = this.getComments(articleId);

    return CompletableFuture.allOf(articleCompletableFuture, commentsCompletableFuture)
        .thenApply(voidResult -> {
          try {
            Article article = articleCompletableFuture.get();
            List<Comment> commentList = commentsCompletableFuture.get();
            article.setComments(commentList);
            return article;
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
          }
        });
  }
}
