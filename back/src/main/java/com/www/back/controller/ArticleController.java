package com.www.back.controller;

import com.www.back.dto.WriteArticleDto;
import com.www.back.entity.Article;
import com.www.back.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
