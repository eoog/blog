package com.www.back.service;

import com.www.back.dto.WriteComment;
import com.www.back.entity.Article;
import com.www.back.entity.Board;
import com.www.back.entity.Comment;
import com.www.back.entity.User;
import com.www.back.exception.ForbiddenException;
import com.www.back.exception.RateLimitException;
import com.www.back.exception.ResourceNotFoundException;
import com.www.back.repository.ArticleRepository;
import com.www.back.repository.BoardRepository;
import com.www.back.repository.CommentRepository;
import com.www.back.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public CommentService(BoardRepository boardRepository, ArticleRepository articleRepository,
      CommentRepository commentRepository, UserRepository userRepository) {
    this.boardRepository = boardRepository;
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  // 댓글 작성
  public Comment writeComment(Long boardId, Long articleId , WriteComment dto) {
    // 사용자 계정 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    if (!this.isCanWriteComment()) {
      throw new RateLimitException("article not written by rate limit");
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
    return comment;

  }


  // 작성 확인
  private boolean isCanWriteComment() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Comment latestComment = commentRepository.findLatestCommentOrderByCreatedDate(userDetails.getUsername());
    if (latestComment == null) {
      return true;
    }
    return this.isDifferenceMoreThanOneMinutes(latestComment.getCreatedDate());
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
  protected CompletableFuture<Article> getArticle(Long boardId , Long articleId) {
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
    return CompletableFuture.completedFuture(article.get());
  }

  // 기사의 모든 댓글
  @Async
  protected CompletableFuture<List<Comment>> getComments(Long articleId) {
    return CompletableFuture.completedFuture(commentRepository.findByArticleId(articleId));
  }

  // 게시판 및 댓글 모두 가져오기
  public CompletableFuture<Article> getArticleWithComments(Long boardId , Long articleId) {
    CompletableFuture<Article> articleCompletableFuture = this.getArticle(boardId,articleId);
    CompletableFuture<List<Comment>> commentsCompletableFuture = this.getComments(articleId);

    return CompletableFuture.allOf(articleCompletableFuture,commentsCompletableFuture)
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
