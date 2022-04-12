package com.model;

/**
* User login result. There has 3 members in this class, include
* status(is login success?), token(jwt token), message.
*
* */
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
