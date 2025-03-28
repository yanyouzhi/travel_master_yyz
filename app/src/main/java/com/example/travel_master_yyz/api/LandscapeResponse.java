package com.example.travel_master_yyz.api;

import java.util.List;

public class LandscapeResponse {
    private int code;
    private String msg;
    private Data data;

    public Data getData() { return data; }

    public static class Data {
        private List<Landscape> records;

        public List<Landscape> getRecords() { return records; }
    }

    public static class Landscape {
        private String name;
        private String description;
        private String image;
        private int praise;
        private int comment;
        private String id;
        private float score;

        private float averageScore;

        public float getAverageScore() {
            return averageScore;
        }

        public String getId() {
            return id;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getImage() { return image; }
        public int getPraise() { return praise; }
        public int getComment() { return comment; }
        public float getScore() { return score; }
    }
}
