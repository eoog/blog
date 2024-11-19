package com.www.back.service;

import com.www.back.entity.Comment;
import com.www.back.pojo.SendCommentNotification;
import com.www.back.pojo.WriteComment;
import com.www.back.repository.ArticleRepository;
import com.www.back.repository.CommentRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiver {

  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;
  private final RabbitMQSender rabbitMQSender;

  @Autowired
  public RabbitMQReceiver(CommentRepository commentRepository,
      ArticleRepository articleRepository, RabbitMQSender rabbitMQSender) {
    this.commentRepository = commentRepository;
    this.articleRepository = articleRepository;
    this.rabbitMQSender = rabbitMQSender;
  }

  @RabbitListener(queues = "onion-notification")
  public void receive(String message) {

    if (message.contains(WriteComment.class.getSimpleName())) {
      this.sendCommentNotification(message);
      return;
    }

    Timer timer = new Timer();

    // 10초후에 실행될 작업 Timer 등록

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Receieved Message: " + message);
      }
    }, 5000);

  }

  private void sendCommentNotification(String message) {
    message = message.replace("WriteComment(", "").replace(")", "");
    String[] parts = message.split(", ");
    String type = null;
    Long commentId = null;
    for (String part : parts) {
      String[] keyValue = part.split("=");
      String key = keyValue[0].trim();
      String value = keyValue[1].trim();
      if (key.equals("type")) {
        type = value;
      } else if (key.equals("commentId")) {
        commentId = Long.parseLong(value);
      }
    }
    WriteComment writeComment = new WriteComment();
    writeComment.setType(type);
    writeComment.setCommentId(commentId);

    // 알림 전송
    SendCommentNotification sendCommentNotification = new SendCommentNotification();
    sendCommentNotification.setCommentId(writeComment.getCommentId());
    Optional<Comment> comment = commentRepository.findById(writeComment.getCommentId());
    if (comment.isEmpty()) {
      return;
    }
    HashSet<Long> userSet = new HashSet<>();
    // 댓글 작성한 본인
    userSet.add(comment.get().getAuthor().getId());
    // 글 작성자
    userSet.add(comment.get().getArticle().getAuthor().getId());
    // 댓글 작성자 모두
    List<Comment> comments = commentRepository.findByArticleId(comment.get().getArticle().getId());
    for (Comment article_comment : comments) {
      userSet.add(article_comment.getAuthor().getId());
    }
    for (Long userId : userSet) {
      sendCommentNotification.setUserId(userId);
      rabbitMQSender.send(sendCommentNotification);
    }
  }


}
