package com.service;

import com.dao.AchievementRecoardDao;
import com.model.AchievementRecord;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    /**
     * Get user all achievement records
     * @param username the username
     * @return result
     */
    public Object getUserAchievements(String username) {

        String collectionName = username+ "_achievement_record";
        CollectionNameHolder.set(collectionName);
        List<AchievementRecord> achievementRecords = achievementRecoardDao.findAll();
        if(achievementRecords != null){
            return achievementRecords;
        }
        else{
            return new JSONObject().appendField("message","Cannot found user achievement record");
        }

    }
}
