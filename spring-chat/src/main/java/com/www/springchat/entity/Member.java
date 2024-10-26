package com.www.springchat.entity;

import com.www.springchat.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    @Column(name = "memeber_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;
    String nickName;
    String name;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String phoneNumber;;
    LocalDate birthday;
    String role;
}
