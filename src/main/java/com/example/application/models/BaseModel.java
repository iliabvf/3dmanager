package com.example.application.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseModel {
    public Integer id;
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
