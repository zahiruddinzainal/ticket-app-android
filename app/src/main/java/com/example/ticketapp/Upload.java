package com.example.ticketapp;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mDestination;
    private String mTime;
    private String mHarga;
    private String mImageUrl;
    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String destination, String time, String harga, String imageUrl) {

        mDestination = destination;
        mTime = time;
        mHarga = harga;
        mImageUrl = imageUrl;
    }

    public String getDestination() {
        return mDestination;
    }


    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getTime() {
        return mTime;
    }


    public void setTime(String time) {
        mTime = time;
    }


    public String getHarga() {

        return mHarga;
    }

    public void setHarga(String harga) {
        mHarga = harga;
    }

    public String getImageUrl() {

        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {

        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {

        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}