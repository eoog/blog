package com.www.userservice.repository;

import com.www.userservice.entity.User;
import com.www.userservice.entity.UserLoginHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Integer> {

  List<UserLoginHistory> findByUserOrderByLoginTimeDesc(User user);
}
