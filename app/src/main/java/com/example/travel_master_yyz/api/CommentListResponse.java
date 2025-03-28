package com.example.travel_master_yyz.api;

import java.util.List;

public class CommentListResponse {
    private int code;
    private String msg;
    private Data data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private List<Comment> records;
        private int total;
        private int size;
        private int current;
        private int pages;

        public List<Comment> getRecords() {
            return records;
        }

        public int getTotal() {
            return total;
        }

        public int getSize() {
            return size;
        }

        public int getCurrent() {
            return current;
        }

        public int getPages() {
            return pages;
        }
    }

    public static class Comment {
        private String fid;
        private String uid;
        private String name;
        private String comment;
        private String photo;
        private int id;
        private String time;

        public String getFid() {
            return fid;
        }

        public String getPhoto() {
            return photo;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public int getId() {
            return id;
        }

        public String getTime() {
            return time;
        }
    }
}
