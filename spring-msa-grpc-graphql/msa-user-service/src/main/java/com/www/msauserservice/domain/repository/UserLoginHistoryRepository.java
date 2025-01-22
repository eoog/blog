package com.www.msauserservice.domain.repository;

import com.www.msauserservice.domain.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Integer> {

}
