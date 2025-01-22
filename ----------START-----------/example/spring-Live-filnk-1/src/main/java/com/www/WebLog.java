package com.www;

import java.util.Date;
import lombok.Data;

@Data
public class WebLog {
  private String ipAddress;
  private String url;
  private Date timestamp;
  private String userId;
  private String sessionId;
}
