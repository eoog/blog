package com.www.msauserservice.domain.controller;


import com.www.msauserservice.domain.dto.PasswordChangeDTO;
import com.www.msauserservice.domain.dto.UserDTO;
import com.www.msauserservice.domain.entity.User;
import com.www.msauserservice.domain.entity.UserLoginHistory;
import com.www.msauserservice.domain.exception.DuplicateUserException;
import com.www.msauserservice.domain.exception.UnauthorizedAccessException;
import com.www.msauserservice.domain.exception.UserNotFoundException;
import com.www.msauserservice.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 사용자 생성", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "중복된 사용자 존재", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<User> createUser(
            @Parameter(description = "생성할 사용자 정보", required = true)
            @RequestBody UserDTO userDTO) {
        try {
            User user = userService.createUser(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok(user);
        } catch (DuplicateUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 사용자 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "조회할 사용자의 ID", required = true)
            @PathVariable("userId") Integer userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "사용자 정보 수정", description = "ID로 사용자의 정보를 수정합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 사용자 정보 수정", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "수정할 사용자의 ID", required = true)
            @PathVariable("userId") Integer userId,
            @Parameter(description = "수정할 사용자 정보", required = true)
            @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userId, userDTO.getName(), userDTO.getEmail());
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}/login-histories")
    @Operation(summary = "사용자 로그인 기록 조회", description = "ID로 사용자의 로그인 기록을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 로그인 기록 조회", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserLoginHistory.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<UserLoginHistory>> getUserLoginHistories(
            @Parameter(description = "로그인 기록을 조회할 사용자의 ID", required = true)
            @PathVariable("userId") Integer userId) {
        List<UserLoginHistory> loginHistories = userService.getUserLoginHistory(userId);
        return ResponseEntity.ok(loginHistories);
    }

    @PostMapping("/{userId}/password-change")
    @Operation(summary = "사용자 비밀번호 변경", description = "ID로 사용자의 비밀번호를 변경합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 비밀번호 변경", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "비밀번호를 변경할 사용자의 ID", required = true)
            @PathVariable("userId") Integer userId,
            @Parameter(description = "비밀번호 변경 요청 정보", required = true)
            @RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            userService.changePassword(userId, passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    @Operation(summary = "사용자 없음 예외 처리", description = "사용자 없음 예외를 처리합니다.")
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateUserException.class)
    @Operation(summary = "중복 사용자 예외 처리", description = "중복 사용자 예외를 처리합니다.")
    public ResponseEntity<String> handleDuplicateUser(DuplicateUserException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @Operation(summary = "권한 없음 예외 처리", description = "권한 없음 예외를 처리합니다.")
    public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
