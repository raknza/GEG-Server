package com.dao;

import com.model.LevelRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Configuration
@Repository
public interface LevelRecordDao extends MongoRepository<LevelRecord, String> {

    @Query(value=("{'username':?0}"))
    public LevelRecord findByUsername(String username);
}
