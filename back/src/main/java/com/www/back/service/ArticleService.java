package com.www.back.service;

import com.www.back.dto.WriteArticleDto;
import com.www.back.entity.Article;
import com.www.back.entity.Board;
import com.www.back.entity.User;
import com.www.back.exception.ResourceNotFoundException;
import com.www.back.repository.ArticleRepository;
import com.www.back.repository.BoardRepository;
import com.www.back.repository.UserRepository;
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
  public Article writeArticle(WriteArticleDto writeArticleDto) {

    // 시큐리티 로그인으로 가입한 유저 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 시큐리티 인증된 회원 데이터 가져오기
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    // 게시글 작성자
    Optional<User> author = userRepository.findByUsername(userDetails.getUsername());

    // 게시판 조회
    Optional<Board> board = boardRepository.findById(writeArticleDto.getBoardId());

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
}
