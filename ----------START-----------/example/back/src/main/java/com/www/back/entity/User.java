package com.www.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

  // 식별자 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 유저 네임
  @Column(nullable = false)
  private String username;

  // 패스워드
  @Column(nullable = false)
  @JsonIgnore
  private String password;

  // 이메일
  @Column(nullable = false)
  @JsonIgnore
  private String email;

  // 마지막 로그인
  private LocalDateTime lastLogin;

  // 회원 생성 날자
  @CreatedDate
  @Column(insertable = true)
  private LocalDateTime createdDate;

  // 마지막으로 수정된 시간
  @LastModifiedDate
  private LocalDateTime updateDate;

  @Column(columnDefinition = "json")
  @Convert(converter = DeviceListConverter.class)
  private List<Device> deviceList = new ArrayList<>();

  // 이 애너테이션은 엔티티가 데이터베이스에 처음 저장되기 전에 호출됩니다.
  // onCreate 메서드는 엔티티가 처음 저장될 때 createdDate 필드에 현재 시간을 설정합니다. 이는 엔티티가 생성된 날짜와 시간을 기록하는 데 사용됩니다.
  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDateTime.now();
    if (deviceList == null) {
      deviceList = new ArrayList<>(); // 기본값을 빈 배열로 설정
    }
  }

  // 이 애너테이션은 엔티티가 데이터베이스에 업데이트되기 전에 호출됩니다.
  // onUpdate 메서드는 엔티티가 수정될 때 updateDate 필드에 현재 시간을 설정합니다. 이는 엔티티가 마지막으로 수정된 날짜와 시간을 기록하는 데 사용됩니다.
  @PreUpdate
  protected void onUpdate() {
    this.updateDate = LocalDateTime.now();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
