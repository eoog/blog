package com.www.back.controller;

import com.www.back.dto.WriteComment;
import com.www.back.entity.Comment;
import com.www.back.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }
  
  // 댓글 등록
  @PostMapping("/{boardId}/articles/{articleId}")
  public ResponseEntity<Comment> writeComment(
      @PathVariable Long boardId,
      @PathVariable Long articleId,
      @RequestBody WriteComment writeComment
  ) {
    return ResponseEntity.ok(commentService.writeComment(boardId,articleId,writeComment));
  }
    
}
