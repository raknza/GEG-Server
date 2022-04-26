package com.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{T(com.utils.CollectionNameHolder).get()}")
public class LevelRecord extends BaseEntity {

    private String username;
    private String time;
    private int time_cost;
    private int line_cost;

    public LevelRecord(){
        super();
    }

    /**
     * Model of User, Clients must passed name, username and password.
     * Password will be encode to MD5 string in constructor.
     *
     */
    public LevelRecord(String username, String time, int timeCost, int lineCost){
        super();
        this.username = username;
        this.time = time;
        this.time_cost = timeCost;
        this.line_cost = lineCost;
    }


    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public int getTimeCost(){ return time_cost; }
    public void setTimeCost(int timeCost){
        this.time_cost = timeCost;
    }

    public String getTime(){ return time; }
    public void setTime(String time){ this.time = time; }

    public int getLineCost(){
        return line_cost;
    }
    public void setLineCost(int lineCost){
        this.line_cost = lineCost;
    }


}
