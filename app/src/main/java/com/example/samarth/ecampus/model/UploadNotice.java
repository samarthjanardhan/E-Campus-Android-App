package com.example.samarth.ecampus.model;

import com.google.firebase.database.Exclude;

public class UploadNotice {

    private String mName;
    private String mImageUrl;
    private String mkey;

    public UploadNotice() {
        //empty constructor needed
    }

    public UploadNotice(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude                //exclude from database to avoid redundant data
    public String getkey()
    {
        return mkey;
    }
    @Exclude
    public void setkey(String key){
        mkey = key;
    }
}
