package com.www.msagraphql.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private Long id;
  private String name;
  private String email;
  private String passwordHash;

  public User(Long userId, String name, String mail) {
    this.id = userId;
    this.name = name;
    this.email = mail;
  }
}
