package com.www.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.www.back.dto.EditArticleDto;
import com.www.back.dto.WriteArticleDto;
import com.www.back.entity.Article;
import com.www.back.service.ArticleService;
import com.www.back.service.CommentService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class ArticleController {

  private final AuthenticationManager authenticationManager;
  private final ArticleService articleService;
  private final CommentService commentService;

  @Autowired
  public ArticleController(AuthenticationManager authenticationManager,
      ArticleService articleService, CommentService commentService) {
    this.authenticationManager = authenticationManager;
    this.articleService = articleService;
    this.commentService = commentService;
  }

  // 게시글 작성
  @PostMapping("{boardId}/articles")
  public ResponseEntity<Article> writeArticle(@PathVariable Long boardId,
      @RequestBody WriteArticleDto writeArticleDto)
      throws JsonProcessingException {
    return ResponseEntity.ok(articleService.writeArticle(boardId, writeArticleDto));
  }

  // 게시글 조회
  @GetMapping("/{boardId}/articles")
  public ResponseEntity<List<Article>> getArticle(@PathVariable Long boardId,
      @RequestParam(required = false) Long lastId, // 마지막 게시글
      @RequestParam(required = false) Long firstId // 첫번째 게시글
  ) {

    if (lastId != null) {
      return ResponseEntity.ok(articleService.getOldArticle(boardId, lastId));
    }
    if (firstId != null) {
      return ResponseEntity.ok(articleService.getNewArticle(boardId, firstId));
    }

    return ResponseEntity.ok(articleService.firstGetArticle(boardId));
  }

  // 검색

  @GetMapping("/{boardId}/articles/search")
  public ResponseEntity<List<Article>> searchArticle(@PathVariable Long boardId,
      @RequestParam(required = true) String keyword) {
    if (keyword != null) {
      return ResponseEntity.ok(articleService.searchArticle(keyword));
    }
    return ResponseEntity.ok(articleService.firstGetArticle(boardId));
  }

  // 게시글 수정
  @PutMapping("/{boardId}/articles/{articleId}")
  public ResponseEntity<Article> editArticle(@PathVariable Long boardId,
      @PathVariable Long articleId,
      @RequestBody EditArticleDto editArticleDto) throws JsonProcessingException {
    return ResponseEntity.ok(articleService.editArticle(boardId, articleId, editArticleDto));
  }

  // 게시글 삭제
  @DeleteMapping("/{boardId}/articles/{articleId}")
  public ResponseEntity<String> deleteArticle(@PathVariable Long boardId,
      @PathVariable Long articleId)
      throws JsonProcessingException {
    articleService.deleteArticle(boardId, articleId);
    return ResponseEntity.ok("article deleted");
  }

  // 게시글과 댓글 전체 가져오기
  public ResponseEntity<Article> getArticleWithComment(
      @PathVariable Long boardId,
      @PathVariable Long articleId
  ) throws ExecutionException, InterruptedException, JsonProcessingException {
    CompletableFuture<Article> article = commentService.getArticleWithComments(boardId, articleId);
    return ResponseEntity.ok(article.get());
  }

}
