package com.example.travel_master_yyz.api;

import java.util.List;

public class CommunityResponse {
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
        private List<CommunityPost> records;
        private int total;
        private int size;
        private int current;
        private int pages;

        public List<CommunityPost> getRecords() {
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
}

