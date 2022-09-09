package com.example.application.models;

public class ProjectFiles extends Projects{
    int id;
    int project;
    String fullFileName;
    String fileName;

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProjectFiles(int id, int project, String fullFileName, String fileName) {
        this.id = id;
        this.project = project;
        this.fullFileName = fullFileName;
        this.fileName = fileName;
    }
}
