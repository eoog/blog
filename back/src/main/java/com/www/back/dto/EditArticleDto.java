package com.www.back.dto;

import java.util.Optional;
import lombok.Getter;

@Getter
public class EditArticleDto {
  Optional<String> title;
  Optional<String> content;
}
