package com.www.videoredis.adapter.out.jpa.user;

import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<UserJpaEntity, String> {
}
