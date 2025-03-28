package com.example.travel_master_yyz.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class CommentResponse {
    private int code;
    private String msg;
    private Data data;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public Data getData() { return data; }

    public static class Data {
        @SerializedName("records")
        private List<CommentData> records;
        private int total;
        private int size;
        private int current;
        private int pages;

        public List<CommentData> getRecords() { return records; }
        public int getTotal() { return total; }
        public int getSize() { return size; }
        public int getCurrent() { return current; }
        public int getPages() { return pages; }
    }

    public static class CommentData {
        private String photo;
        private String fid;
        private String uid;
        private String name;
        private String comment;  // 这里需要转换
        private int id;
        private String time;

        public String getPhoto() {
            return photo;
        }

        public String getFid() { return fid; }
        public String getUid() { return uid; }
        public String getName() { return name; }
        public String getRawComment() { return comment; } // 原始 JSON 字符串
        public int getId() { return id; }
        public String getTime() { return time; }

        // 解析 comment 字段
        public String getComment() {
            try {
                JSONArray jsonArray = new JSONArray(comment);
                return jsonArray.length() > 0 ? jsonArray.getString(0) : "";
            } catch (JSONException e) {
                e.printStackTrace();
                return comment; // 解析失败，返回原字符串
            }
        }
    }
}
