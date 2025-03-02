package com.example.travel_master_yyz.api;

import com.example.travel_master_yyz.Dao.UserData;

public class LoginResponse {
    private int code;
    private String msg;
    private UserData data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public UserData getData() {
        return data;
    }
}
