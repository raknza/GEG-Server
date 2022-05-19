package com.model;

/**
 * Model of User, data will be saved in collection 'Users'
 *
 */
public class User extends BaseEntity{

    private String username;
    private String name;
    private String password;
    private String role;

    public User(){
        super();
    }

    /**
     * Constructor of User class, Clients must passed name, username and password.
     *
     */
    public User(String name, String username, String password, String role){
        super();
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public boolean checkPassword(String password){ return password.equals(this.password); }



}
