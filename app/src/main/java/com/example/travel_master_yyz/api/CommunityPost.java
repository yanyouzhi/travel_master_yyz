package com.example.travel_master_yyz.api;

public class CommunityPost {
    private String uid;
    private String image;
    private String photo;
    private String description;
    private String id;
    private String time;
    private int community;
    private String name;  // 用户名
    private int praise;   // 点赞数
    private int comment;  // 评论数
    private int praise_my; // 是否点赞

    public String getUid() {
        return uid;
    }

    public String getImage() {
        return image;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public int getCommunity() {
        return community;
    }

    public String getName() {
        return name;
    }

    public int getPraise() {
        return praise;
    }

    public int getComment() {
        return comment;
    }

    public int getPraise_my() {
        return praise_my;
    }
}
