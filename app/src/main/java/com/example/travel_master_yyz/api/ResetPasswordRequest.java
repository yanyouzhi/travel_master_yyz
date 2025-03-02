package com.example.travel_master_yyz.api;

public class ResetPasswordRequest {
    private String email;
    private String pwd;
    private String verify;

    public ResetPasswordRequest(String email, String pwd, String verify) {
        this.email = email;
        this.pwd = pwd;
        this.verify = verify;
    }
}
