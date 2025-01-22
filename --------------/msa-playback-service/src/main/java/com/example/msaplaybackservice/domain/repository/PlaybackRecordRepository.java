package com.example.msaplaybackservice.domain.repository;

import com.example.msaplaybackservice.domain.entity.PlaybackRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaybackRecordRepository extends JpaRepository<PlaybackRecord, Long> {
}
