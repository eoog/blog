package com.www.msagraphql.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginHistory implements Serializable {
    private Long id;
    private String userId;
    private String loginTime;
    private String ipAddress;
}
