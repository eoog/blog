package com.www.back.controller;

import com.www.back.dto.WriteDeviceDto;
import com.www.back.entity.Device;
import com.www.back.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

  private final UserService userService;

  @Autowired
  public DeviceController(UserService userService) {
    this.userService = userService;
  }

  // 디바이스 조회
  @GetMapping("/")
  public ResponseEntity<List<Device>> getDevices() {
    return ResponseEntity.of(Optional.ofNullable(userService.getDevices()));
  }

  // 디바이스 등록
  @PostMapping("")
  public ResponseEntity<Device> addDevice(@RequestBody WriteDeviceDto writeDeviceDto) {
    return ResponseEntity.ok(userService.addDevice(writeDeviceDto));
  }
}
