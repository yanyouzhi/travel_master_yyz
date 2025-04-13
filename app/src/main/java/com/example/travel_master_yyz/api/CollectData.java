package com.example.travel_master_yyz.api;

import java.util.List;

public class CollectData {
    private List<LandscapeCollect> records;
    private int total;
    private int size;
    private int current;
    private int pages;

    public List<LandscapeCollect> getRecords() { return records; }
    public int getTotal() { return total; }
    public int getSize() { return size; }
    public int getCurrent() { return current; }
    public int getPages() { return pages; }
}
