package com.www.back.repository;

import com.www.back.entity.AdClickHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdClickHistoryRepository extends MongoRepository<AdClickHistory, String> {

}
