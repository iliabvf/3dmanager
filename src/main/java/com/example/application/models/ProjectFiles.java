package com.example.application.models;

public class ProjectFiles extends Projects {
    Integer project;

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public ProjectFiles(Integer id, String name) {
        super(id, name);
    }
}
