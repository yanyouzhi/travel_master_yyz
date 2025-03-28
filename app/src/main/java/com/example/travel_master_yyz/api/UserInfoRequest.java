package com.example.travel_master_yyz.api;

public class UserInfoRequest {
    private String uid;
    private String name;
    private String photo;
    private int sex;
    private String addr;
    private String description;
    private String occupation;
    private String contact;

    public UserInfoRequest(String uid, String name, String photo, int sex, String addr, String description, String occupation, String contact) {
        this.uid = uid;
        this.name = name;
        this.photo = photo;
        this.sex = sex;
        this.addr = addr;
        this.description = description;
        this.occupation = occupation;
        this.contact = contact;
    }
}
