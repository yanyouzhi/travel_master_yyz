package com.example.travel_master_yyz.api;

public class DiaryPost {
    private String id;
    private String uid;
    private String name;
    private String description;
    private String time;
    private String addr;
    private int praise;
    private int comment;
    private String photo; // 头像 Base64
    private String image; // 文章图片 Base64

    public String getUid() {
        return uid;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTime() { return time; }
    public String getAddr() { return addr; }
    public int getPraise() { return praise; }
    public int getComment() { return comment; }
    public String getPhoto() { return photo; }
    public String getImage() { return image; }
}
