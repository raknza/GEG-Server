package com.service;

import com.dao.LevelRecordDao;
import com.model.LevelRecord;
import com.model.Statistics;
import com.utils.DateHelper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import net.minidev.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan({"com.dao"})
@PropertySource(value = "classpath:mongo_collection.properties")
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
        levelRecordDao.setLevel(level);
        LevelRecord oldRecord = levelRecordDao.findByUsername(username);
        LevelRecord newLevelRecord = new LevelRecord(username, nowTime, timeCost, lineCost);
        Object result;
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
        levelRecordDao.setLevel(level);
        return levelRecordDao.findAll();
    }

    /**
     * Get statistics time cost of level
     * @param level the level
     * @return result
     */
    public Statistics getLevelPassedTimeCostStatistics(int level){
        levelRecordDao.setLevel(level);
        List<LevelRecord> allRecords = levelRecordDao.findAll();
        List<Integer> allTimeCost = new ArrayList<>();
        for(int i=0; i< allRecords.size();i++){
            allTimeCost.add(allRecords.get(i).getTimeCost());
        }
        return new Statistics(allTimeCost);
    }

    /**
     * Get statistics time cost of level
     * @param start the start of range
     * @param end the end of range
     * @return result
     */
    public List<Statistics> getLevelPassedTimeCostStatistics(int start,int end){
        List<Statistics> allTimeCostStatistics= new ArrayList<>();
        for (int level = start ; level <= end ; level++){
            allTimeCostStatistics.add(getLevelPassedTimeCostStatistics(level));
        }
        return allTimeCostStatistics;
    }

}
