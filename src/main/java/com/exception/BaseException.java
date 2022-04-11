package com.exception;

import net.minidev.json.JSONObject;

public class BaseException extends Exception {

    public BaseException(String message){
        super(message);
    }

    public JSONObject toJson(){
        return new JSONObject().appendField("error",this.getMessage());
    }
}
