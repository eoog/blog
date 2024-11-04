package com.www.back.repository;

import com.www.back.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  // 댓글 작성
  @Query("SELECT c FROM Comment c JOIN c.author u WHERE u.username = :username ORDER BY c.createdDate DESC LIMIT 1")
  Comment findLatestCommentOrderByCreatedDate(@Param("username") String username);

  @Query("SELECT c FROM Comment c JOIN c.author u WHERE u.username = :username ORDER BY c.updatedDate DESC LIMIT 1")
  Comment findLatestCommentOrderByUpdatedDate(@Param("username") String username);

  // 댓글 조회
  @Query("SELECT c FROM Comment c WHERE c.article.id = :articleId AND c.isDeleted = false")
  List<Comment> findByArticleId(@Param("articleId") Long articleId);
}
