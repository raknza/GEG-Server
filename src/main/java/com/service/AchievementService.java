package com.service;

import com.dao.AchievementRecoardDao;
import com.model.AchievementRecord;
import com.model.LevelRecord;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableMongoRepositories
@ComponentScan({"com.utils"})
public class AchievementService {

    private final AchievementRecoardDao achievementRecoardDao;

    public AchievementService(AchievementRecoardDao achievementRecoardDao){
        this.achievementRecoardDao = achievementRecoardDao;
    }

    /**
     * Log user achievement record
     * @param username the username
     * @param achievement the achievement object
     * @return result
     */
    public Object logAchievement(String username, String achievement) {

        String collectionName = username+ "_achievement_record";
        CollectionNameHolder.set(collectionName);
        String nowTime = DateHelper.getNowTime();
        AchievementRecord newRecord = new AchievementRecord(nowTime,achievement);
        AchievementRecord oldRecord = achievementRecoardDao.findByAchievementId(newRecord.getAchievement().getInteger("id"));
        if(oldRecord == null){
            return achievementRecoardDao.insert(newRecord);
        }
        else{
            return new JSONObject().appendField("message","Already have this achievement");
        }

    }
}
