package com.www.back.service;

import com.www.back.dto.EditArticleDto;
import com.www.back.dto.WriteArticleDto;
import com.www.back.entity.Article;
import com.www.back.entity.Board;
import com.www.back.entity.User;
import com.www.back.exception.ForbiddenException;
import com.www.back.exception.RateLimitException;
import com.www.back.exception.ResourceNotFoundException;
import com.www.back.repository.ArticleRepository;
import com.www.back.repository.BoardRepository;
import com.www.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  private final BoardRepository boardRepository;
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  @Autowired
  public ArticleService(BoardRepository boardRepository, ArticleRepository articleRepository,
      UserRepository userRepository) {
    this.boardRepository = boardRepository;
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;
  }

  // 1. 게시글 작성
  @Transactional
  public Article writeArticle(Long boardId, WriteArticleDto writeArticleDto) {

    // 시큐리티 로그인으로 가입한 유저 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 시큐리티 인증된 회원 데이터 가져오기
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//    if (!this.isCanWriteArticle()) {
//      throw new RateLimitException("article not written by rate limit");
//    }

    // 게시글 작성자
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());

    // 게시판 조회
    Optional<Board> board = boardRepository.findById(boardId);

    // 작성자 없을대
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    // 게시판 없을대
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }

    // 게시글 작성
    Article article = new Article();
    article.setBoard(board.get());
    article.setAuthor(author.get());
    article.setTitle(writeArticleDto.getTitle());
    article.setContent(writeArticleDto.getContent());
    articleRepository.save(article);
    return article;
  }

  // 게시글 최신 게시글 10개 가져오기
  public List<Article> firstGetArticle(Long boardId) {
    return articleRepository.findTop10ByBoardIdOrderByCreatedDateDesc(boardId);
  }

  // 게시글 예전꺼 10개 가져오기
  public List<Article> getOldArticle(Long boardId, Long articleId) {
    return articleRepository.findTop10ByBoardIdAndArticleIdLessThanOrderByCreatedDateDesc(boardId,articleId);
  }

  // 게시글 새로운 10개 가져오기
  public List<Article> getNewArticle(Long boardId, Long articleId) {
    return articleRepository.findTop10ByBoardIdAndArticleIdGreaterThanOrderByCreatedDateDesc(boardId, articleId);
  }

  // 게시글 수정 !!
  @Transactional
  public Article editArticle(Long boardId, Long articleId, EditArticleDto dto) {

    // 시큐리티 로그인으로 가입한 유저 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 시큐리티 인증된 회원 데이터 가져오기
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    // 게시글 작성자
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());

    // 게시판 조회
    Optional<Board> board = boardRepository.findById(boardId);

    // 게시글 또는 게시판 없을때
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }

    // 게시글
    Optional<Article> article = articleRepository.findById(articleId);

    if (article.get().getAuthor() != author.get()) {
      throw new ForbiddenException("article author different");
    }
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    if (!this.isCanEditArticle()) {
      throw new RateLimitException("article not edited by rate limit");
    }

    // 게시글 수정하기 타이틀 및 내용
    if (dto.getTitle() != null) {
      article.get().setTitle(dto.getTitle().get());
    }
    if (dto.getContent() != null) {
      article.get().setContent(dto.getContent().get());
    }

    articleRepository.save(article.get());
    return article.get();
  }

  // 게시글 삭제
  @Transactional
  public Boolean deleteArticle(Long boardId, Long articleId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());
    Optional<Board> board = boardRepository.findById(boardId);
    if (author.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }
    if (board.isEmpty()) {
      throw new ResourceNotFoundException("board not found");
    }
    Optional<Article> article = articleRepository.findById(articleId);
    if (article.isEmpty()) {
      throw new ResourceNotFoundException("article not found");
    }
    if (article.get().getAuthor() != author.get()) {
      throw new ForbiddenException("article author different");
    }
    if (!this.isCanEditArticle()) {
      throw new RateLimitException("article not edited by rate limit");
    }
    
    // 삭제 true 변경
    article.get().setIsDeleted(true);
    articleRepository.save(article.get());
    return true;
  }

  // 게시글 작성 여부 확인 최신생설날자
  private boolean isCanWriteArticle() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Article latestArticle = articleRepository.findLatestArticleByAuthorUsernameOrderByCreatedDate(userDetails.getUsername());
    return this.isDifferenceMoreThanFiveMinutes(latestArticle.getCreatedDate());
  }

  // 가장 최신 글 1건 조회
  private boolean isCanEditArticle() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Article latestArticle = articleRepository.findLatestArticleByAuthorUsernameOrderByUpdatedDate(userDetails.getUsername());
    return this.isDifferenceMoreThanFiveMinutes(latestArticle.getUpdatedDate());
  }

  // 게시글 작성한지 5분이상 지났는지 확인
  private boolean isDifferenceMoreThanFiveMinutes(LocalDateTime localDateTime) {
    LocalDateTime dateAsLocalDateTime = new Date().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    Duration duration = Duration.between(localDateTime, dateAsLocalDateTime);

    return Math.abs(duration.toMinutes()) > 5;
  }
  
}
