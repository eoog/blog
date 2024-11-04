package com.www.back.controller;

import com.www.back.dto.WriteArticleDto;
import com.www.back.entity.Article;
import com.www.back.service.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class ArticleController {

  private final AuthenticationManager authenticationManager;
  private final ArticleService articleService;

  @Autowired
  public ArticleController(AuthenticationManager authenticationManager,
      ArticleService articleService) {
    this.authenticationManager = authenticationManager;
    this.articleService = articleService;
  }

  // 게시글 작성
  @PostMapping("{boardId}/articles")
  public ResponseEntity<Article> writeArticle(@RequestBody WriteArticleDto writeArticleDto) {
      return ResponseEntity.ok(articleService.writeArticle(writeArticleDto));
  }

  // 게시글 조회
  @GetMapping("/{boardId}/articles")
  public ResponseEntity<List<Article>> getArticle(@PathVariable Long boardId,
      @RequestParam(required = false) Long lastId, // 마지막 게시글
      @RequestParam(required = false) Long firstId // 첫번째 게시글
  ) {

    if (lastId != null) {
      return ResponseEntity.ok(articleService.getOldArticle(boardId , lastId));
    }
    if (firstId != null) {
      return ResponseEntity.ok(articleService.getNewArticle(boardId , firstId));
    }

    return ResponseEntity.ok(articleService.firstGetArticle(boardId));
  }
}
