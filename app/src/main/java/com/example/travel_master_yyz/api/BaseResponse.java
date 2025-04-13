package com.example.travel_master_yyz.api;

public class BaseResponse<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public T getData() { return data; }

    public boolean isSuccess() {
        return code == 200;
    }
}
