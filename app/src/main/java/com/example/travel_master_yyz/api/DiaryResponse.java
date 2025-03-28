package com.example.travel_master_yyz.api;

import com.example.travel_master_yyz.model_mvvm.DiaryPost;

import java.util.List;

public class DiaryResponse {
    private int code;
    private String msg;
    private Data data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private List<DiaryPost> records;
        private int total;
        private int size;
        private int current;
        private int pages;

        public List<DiaryPost> getRecords() { return records; }
        public void setRecords(List<DiaryPost> records) { this.records = records; }

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public int getCurrent() { return current; }
        public void setCurrent(int current) { this.current = current; }

        public int getPages() { return pages; }
        public void setPages(int pages) { this.pages = pages; }
    }
}
