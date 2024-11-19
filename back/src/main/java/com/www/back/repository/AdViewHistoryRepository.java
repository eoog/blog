package com.www.back.repository;

import com.www.back.entity.AdViewHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdViewHistoryRepository extends MongoRepository<AdViewHistory, String> {

}
