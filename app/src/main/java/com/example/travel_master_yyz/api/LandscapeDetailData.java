package com.example.travel_master_yyz.api;

public class LandscapeDetailData {
    private String image;
    private String description;
    private int praise;
    private int score;
    private String name;
    private int comment;
    private int collect_my;
    private int praise_my;

    private float averageScore;
    private int score_my;

    public int getScore_my() {
        return score_my;
    }

    public int getCollect_my() {
        return collect_my;
    }

    public int getPraise_my() {
        return praise_my;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getPraise() {
        return praise;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getComment() {
        return comment;
    }
}
