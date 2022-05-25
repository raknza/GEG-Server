package com.model;

import org.bson.Document;

public class UserEvent extends BaseEntity {

    private String username;
    private String eventName;
    private String ip;
    private String time;
    private Document eventContent;

    public UserEvent(){
        super();
    }

    /**
     * Model of User, Clients must passed name, username and password.
     * Password will be encode to MD5 string in constructor.
     *
     */
    public UserEvent(String username, String eventName,String ip, String time, String eventContent){
        super();
        this.username = username;
        this.eventName = eventName;
        this.ip = ip;
        this.time = time;
        this.eventContent = Document.parse(eventContent);
    }

    /**
     * Model of User, Clients must passed name, username and password.
     * Password will be encode to MD5 string in constructor.
     *
     */
    public UserEvent(String username, String eventName,String ip, String time, Document eventContent){
        super();
        this.username = username;
        this.eventName = eventName;
        this.ip = ip;
        this.time = time;
        this.eventContent = eventContent;
    }


    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getEventName(){ return eventName; }
    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public String getIp(){
        return ip;
    }
    public void setIp(String ip){
        this.ip = ip;
    }

    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    public org.bson.Document getEventContent(){
        return eventContent;
    }
    public void setEventContent(org.bson.Document eventContent){
        this.eventContent = eventContent;
    }

}
