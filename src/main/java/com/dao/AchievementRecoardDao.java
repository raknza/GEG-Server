package com.dao;

import com.model.AchievementRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Configuration
@Repository
public interface AchievementRecoardDao extends MongoRepository<AchievementRecord, String> {

    @Query(value=("{'achievement.id':?0}"))
    public AchievementRecord findByAchievementId(int id);
}
