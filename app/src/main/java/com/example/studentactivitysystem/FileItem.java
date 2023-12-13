package com.example.studentactivitysystem;

public class FileItem {
    private final String fileName;
    private final String fileUrl;

    public FileItem(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
