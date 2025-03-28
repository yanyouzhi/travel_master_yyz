package com.example.travel_master_yyz.api;

import java.util.List;

public class CommentRequest {
    private List<String> comment;

    public CommentRequest(List<String> comment) {
        this.comment = comment;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }
}
