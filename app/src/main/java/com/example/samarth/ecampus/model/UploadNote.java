package com.example.samarth.ecampus.model;

public class UploadNote {

    private String mName;
    private String mpdfUrl;

    public UploadNote() {
        //empty constructor needed
    }

    public UploadNote(String name, String pdfUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mpdfUrl = pdfUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getpdfUrl() {
        return mpdfUrl;
    }

    public void setpdfUrl(String pdfUrl) {
        mpdfUrl = pdfUrl;
    }
}
