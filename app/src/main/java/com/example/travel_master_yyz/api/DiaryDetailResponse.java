package com.example.travel_master_yyz.api;

import com.google.gson.annotations.SerializedName;

public class DiaryDetailResponse {
    private int code;
    private String msg;
    private Data data;

    public static class Data {
        private String name;
        private String description;
        private String time;
        private String image;
        private int praise;
        private String id;
        private int comment;
        @SerializedName("praise_my")
        private int praise_my;
        private int praise_be;

        private String uid;
        private String photo;

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public String getPhoto() {
            return photo;
        }

        public int getPraise_my() {
            return praise_my;
        }

        public int getPraise_be() {
            return praise_be;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getTime() { return time; }
        public String getImage() { return image; }
        public int getPraise() { return praise; }
        public int getComment() { return comment; }
    }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public Data getData() { return data; }
}
