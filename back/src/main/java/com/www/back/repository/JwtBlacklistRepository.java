package com.www.back.repository;

import com.www.back.entity.JwtBlacklist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findTopByUsernameOrderByExpirationTime(String username);
}
