package com.example.travel_master_yyz.model_mvvm;

import java.util.List;

public class CommunityResponse {
    private int code;
    private String msg;
    private Data data;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public Data getData() { return data; }

    public static class Data {
        private List<DiaryPost> records;
        private int total, size, current, pages;

        public List<DiaryPost> getRecords() { return records; }
    }
}

