package com.example.travel_master_yyz.dao;

public class LandscapeCacheItem {
    public String id;
    public String uid;
    public String image;
    public String description;
    public int praise;
    public String name;
    public long timestamp;

    public LandscapeCacheItem(String id, String uid, String image, String description, int praise, String name, long timestamp) {
        this.id = id;
        this.uid = uid;
        this.image = image;
        this.description = description;
        this.praise = praise;
        this.name = name;
        this.timestamp = timestamp;
    }

    // 另一个方便用的构造器（用于查询）
    public LandscapeCacheItem(String id, String image, String description, int praise, String name, long timestamp) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.praise = praise;
        this.name = name;
        this.timestamp = timestamp;
    }
}
