package com.example.application.models;

public class Projects{
    int id;
    String name;

    public Projects(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public Projects() {
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

