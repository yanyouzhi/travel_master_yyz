package com.example.travel_master_yyz.api;

public class AddCommentRequest {
    private String comment;

    public AddCommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
