package com.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{T(com.utils.CollectionNameHolder).get()}")
public class UserEvent extends BaseEntity {

    private String username;
    private String event_name;
    private String ip;
    private String time;
    private org.bson.Document event_content;

    public UserEvent(){
        super();
    }

    /**
     * Model of User, Clients must passed name, username and password.
     * Password will be encode to MD5 string in constructor.
     *
     */
    public UserEvent(String username, String eventName,String ip, String time, org.bson.Document eventContent){
        super();
        this.username = username;
        this.event_name = eventName;
        this.ip = ip;
        this.time = time;
        this.event_content = eventContent;
    }


    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getEventName(){ return event_name; }
    public void setEventName(String eventName){
        this.event_name = eventName;
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
        return event_content;
    }
    public void setEventContent(org.bson.Document eventContent){
        this.event_content = eventContent;
    }

}
