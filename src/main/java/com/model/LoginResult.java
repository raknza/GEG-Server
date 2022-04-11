package com.model;

public class LoginResult {
    String token;
    boolean status;
    String message;
    public LoginResult(boolean status, String token,String message) {
        this.token = token;
        this.status = status;
        this.message = message;
    }
    public boolean getStatus(){return status;}
    public String getToken(){return token;}
    public String getMessage(){return message;}
}
