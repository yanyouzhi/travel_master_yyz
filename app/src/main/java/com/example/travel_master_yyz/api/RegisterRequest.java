package com.example.travel_master_yyz.api;

public class RegisterRequest {
    private String email;
    private String phone;
    private String pwd;
    private String verify;

    public RegisterRequest(String email, String phone, String pwd, String verify) {
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
        this.verify = verify;
    }
}
