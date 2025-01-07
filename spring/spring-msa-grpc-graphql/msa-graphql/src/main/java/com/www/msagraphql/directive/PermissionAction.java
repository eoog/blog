package com.www.msagraphql.directive;

import graphql.schema.DataFetchingEnvironment;
import java.util.function.Consumer;
import lombok.Getter;

public class PermissionAction {

  @Getter
  private String permission;
  private final Consumer<DataFetchingEnvironment> action;

  public PermissionAction(String permission, Consumer<DataFetchingEnvironment> action) {
    this.permission = permission;
    this.action = action;
  }

  public void executeAction(DataFetchingEnvironment env) {
    if (action != null) {
      action.accept(env);
    }
  }
}
