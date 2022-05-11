package com.service;

import com.dao.AchievementRecordDao;
import com.model.AchievementRecord;
import com.utils.DateHelper;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ComponentScan({"com.dao"})
public class AchievementService {

    private final AchievementRecordDao achievementRecordDao;

    public AchievementService(AchievementRecordDao achievementRecordDao){
        this.achievementRecordDao = achievementRecordDao;
    }

    /**
     * Log user achievement record
     * @param username the username
     * @param achievement the achievement object
     * @return result
     */
    public Object logAchievement(String username, String achievement) {

        achievementRecordDao.setUser(username);
        String nowTime = DateHelper.getNowTime();
        AchievementRecord newRecord = new AchievementRecord(nowTime,achievement);
        AchievementRecord oldRecord = achievementRecordDao.findByAchievementId(newRecord.getAchievement().getInteger("id"));
        if(oldRecord == null){
            return achievementRecordDao.insert(newRecord);
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

        achievementRecordDao.setUser(username);
        List<AchievementRecord> achievementRecords = achievementRecordDao.findAll();
        if(achievementRecords != null){
            return achievementRecords;
        }
        else{
            return new JSONObject().appendField("message","Cannot found user achievement record");
        }

    }
}
