package com.www.back.repository;

import com.www.back.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 게시글 조회
  @Query("SELECT a FROM Article a WHERE a.board.id = :boardId AND a.isDeleted = false ORDER BY a.createdDate DESC LIMIT 10")
  List<Article> findTop10ByBoardIdOrderByCreatedDateDesc(@Param("boardId") Long boardId);

  @Query("SELECT a FROM Article a WHERE a.board.id = :boardId AND a.id < :articleId AND a.isDeleted = false ORDER BY a.createdDate DESC LIMIT 10")
  List<Article> findTop10ByBoardIdAndArticleIdLessThanOrderByCreatedDateDesc(@Param("boardId") Long boardId,
      @Param("articleId") Long articleId);

  @Query("SELECT a FROM Article a WHERE a.board.id = :boardId AND a.id > :articleId AND a.isDeleted = false ORDER BY a.createdDate DESC LIMIT 10")
  List<Article> findTop10ByBoardIdAndArticleIdGreaterThanOrderByCreatedDateDesc(@Param("boardId") Long boardId,
      @Param("articleId") Long articleId);

  // 게시글 수정
  @Query("SELECT a FROM Article a JOIN a.author u WHERE u.username = :username ORDER BY a.createdDate DESC LIMIT 1")
  Article findLatestArticleByAuthorUsernameOrderByCreatedDate(@Param("username") String username);

  @Query("SELECT a FROM Article a JOIN a.author u WHERE u.username = :username ORDER BY a.updatedDate DESC LIMIT 1")
  Article findLatestArticleByAuthorUsernameOrderByUpdatedDate(@Param("username") String username);
}
