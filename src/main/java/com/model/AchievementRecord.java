package com.model;

import org.bson.Document;

public class AchievementRecord extends BaseEntity {
    private String time;
    private Document achievement;

    public AchievementRecord(){
        super();
    }

    /**
     * Model of AchievementRecord, when a user get a achievement in game, client
     * will passed a record to log it. Record must include time,
     * achievement object(with json string).
     *
     */
    public AchievementRecord(String time, String achievement){
        super();
        this.time = time;
        this.achievement = Document.parse(achievement);
    }

    public AchievementRecord(String time, Document achievement){
        super();
        this.time = time;
        this.achievement = achievement;
    }

    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    public org.bson.Document getAchievement(){
        return achievement;
    }
    public void setAchievement(org.bson.Document achievement){
        this.achievement = achievement;
    }
}
