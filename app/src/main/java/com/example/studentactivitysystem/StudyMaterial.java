package com.example.studentactivitysystem;

public class StudyMaterial {
    private String title;
    private String description;
    private String fileurl;
    public StudyMaterial() {
        // Default constructor required for Firestore
    }

    public StudyMaterial(String title, String description) {
        this.title = title;
        this.description = description;
        this.fileurl = fileurl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFileUrl() {

        return fileurl;
    }
}
