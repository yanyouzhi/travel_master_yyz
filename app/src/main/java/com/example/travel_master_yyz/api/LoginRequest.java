package com.example.travel_master_yyz.api;

public class LoginRequest {
    private String email;
    private String phone;
    private String pwd;

    public LoginRequest(String email, String phone, String pwd) {
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
    }
}
