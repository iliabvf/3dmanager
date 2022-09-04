package com.example.application.models;

public class SearchItem {
    private String item;
    private int id;
    private String type;

    public SearchItem(String item, int id, String type) {
        this.item = item;
        this.id = id;
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return item;
    }
}
