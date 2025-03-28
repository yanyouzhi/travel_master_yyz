package com.example.travel_master_yyz.api;

public class PostRequest {
    private String uid;
    private String description;
    private String image;
    private String community;

    public PostRequest(String uid, String description, String image, String community) {
        this.uid = uid;
        this.description = description;
        this.image = image;
        this.community = community;
    }
}

