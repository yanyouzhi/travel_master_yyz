package com.example.travel_master_yyz.api;

import java.util.List;

public class FollowDataResponse {
    private int code;
    private String msg;
    private FollowData data;

    // Getter & Setter


    public int getCode() {
        return code;
    }

    public FollowData getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class FollowData {
        private List<FollowRecord> records;
        private int total;
        private int size;
        private int current;
        private int pages;

        // Getter & Setter


        public List<FollowRecord> getRecords() {
            return records;
        }

        public int getTotal() {
            return total;
        }

        public int getSize() {
            return size;
        }

        public int getPages() {
            return pages;
        }

        public int getCurrent() {
            return current;
        }
    }
}
