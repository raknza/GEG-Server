package com.service;

import com.dao.LevelRecordDao;
import com.model.LevelRecord;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import net.minidev.json.JSONObject;

@Service
@EnableMongoRepositories
@ComponentScan({"com.utils"})
public class LevelService {

    private final LevelRecordDao levelRecordDao;

    public LevelService(LevelRecordDao levelRecordDao){
        this.levelRecordDao = levelRecordDao;
    }

    /**
     * Log user level record
     * @param username the username
     * @param timeCost the time cost in level
     * @param lineCost the line cost in level
     * @param level the level which user has passed
     * @return result
     */
    public Object logLevelRecord(String username, int timeCost, int lineCost,int level) {
        String nowTime = DateHelper.getNowTime();
        CollectionNameHolder.set("level" + level + "_record");
        LevelRecord oldRecord = levelRecordDao.findByUsername(username);
        LevelRecord newLevelRecord = new LevelRecord(username, nowTime, timeCost, lineCost);
        Object result = null;
        if (oldRecord != null) {
            // if old record better than the new one
            if ( newLevelRecord.getTimeCost() > oldRecord.getTimeCost() ||
                    ( newLevelRecord.getTimeCost() == oldRecord.getTimeCost() &&
                            newLevelRecord.getLineCost() >= oldRecord.getLineCost()) ){
                return new JSONObject().appendField("message","do not need to update record");
            }
            else{
                // update old record
                newLevelRecord.setId(oldRecord.getId());
                result = levelRecordDao.save(newLevelRecord);
            }
        }
        else { // if not
            result = levelRecordDao.insert(newLevelRecord);
        }
        return result;
    }

    /**
     * Get level leaderboard
     * @param level the level
     * @return leaderboard in the level, and will be ascending order by time cost,
     * line cost and be descending order by time
     */
    public Object getLevelLeaderboard(int level) {
        CollectionNameHolder.set("level" + level + "_record");
        return levelRecordDao.findAll(Sort.by("time_cost","line_cost").ascending().and(Sort.by("time").descending()));
    }

    /**
     * Get number of level has been passed by user
     * @param username the username
     * @param start the start of range
     * @param end the end of range
     * @return result
     */
    public int getUserLevelPassedCount(String username,int start,int end){
        int count = 0;
        for (int level = start ; level <= end ; level++){
            CollectionNameHolder.set("level" + level + "_record");
            if( levelRecordDao.findByUsername(username) != null ){
                count++;
            }
        }
        return count;
    }

}
