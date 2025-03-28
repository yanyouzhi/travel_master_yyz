package com.example.travel_master_yyz.model_mvvm;

public class DiaryPost {
    private String uid;
    private String image;
    private int sex;
    private String name;
    private String description;
    private int comment;
    private String id;
    private String time;
    private int comment_my;
    private int community;
    private int praise_my;
    private int praise;
    private String photo;

    // Getters and Setters

    public String getPhoto() {
        return photo;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getSex() { return sex; }
    public void setSex(int sex) { this.sex = sex; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getComment() { return comment; }
    public void setComment(int comment) { this.comment = comment; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getComment_my() { return comment_my; }
    public void setComment_my(int comment_my) { this.comment_my = comment_my; }

    public int getCommunity() { return community; }
    public void setCommunity(int community) { this.community = community; }

    public int getPraise_my() { return praise_my; }
    public void setPraise_my(int praise_my) { this.praise_my = praise_my; }

    public int getPraise() { return praise; }
    public void setPraise(int praise) { this.praise = praise; }
}

