package com.www.back.controller;

import com.www.back.dto.WriteNotice;
import com.www.back.entity.Notice;
import com.www.back.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

  private final NoticeService noticeService;

  @Autowired
  public NoticeController(NoticeService noticeService) {
    this.noticeService = noticeService;
  }

  // 공지사항 추가
  @PostMapping("")
  public ResponseEntity<Notice> addNotice(@RequestBody WriteNotice dto) {
    return ResponseEntity.ok(noticeService.writeNotice(dto));
  }

  @GetMapping("/{noticeId}")
  public ResponseEntity<Notice> getNotice(@PathVariable Long noticeId) {
    return ResponseEntity.ok(noticeService.getNotice(noticeId));
  }
}
