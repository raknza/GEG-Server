package com.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{T(com.utils.CollectionNameHolder).get()}")
public class AchievementRecord extends BaseEntity {
    private String time;
    private org.bson.Document achievement;

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
        this.achievement = org.bson.Document.parse(achievement);
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
