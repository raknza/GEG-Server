package com.model;

import com.utils.MD5Helper;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Document(collection = "Users")
public class User extends BaseEntity {

    private String username;
    private String name;
    private String password;

    public User(){
        super();
    }

    /**
     * Model of User, Clients must passed name, username and password.
     * Password will be encode to MD5 string in constructor.
     *
     */
    public User(String name, String username, String password){
        super();
        this.name = name;
        this.username = username;
        this.password = MD5Helper.encodeToMD5(password);
    }


    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public boolean checkPassword(String password){ return password.equals(this.password); }



}
