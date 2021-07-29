package com.example.psm;

import com.google.firebase.database.Exclude;

public class Cart {
    private String mName;
    private String mHarga;
    private String mImageUrl;
    private Integer mJumlah;
    private String mKey;

    public Cart() {
        //empty constructor needed
    }

    public Cart(String Nama, String Harga, String Gambar, Integer Jumlah) {

        mName = Nama;
        mHarga = Harga;
        mImageUrl = Gambar;
        mJumlah = Jumlah;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {

        mName = name;
    }

    public String getHarga() {

        return mHarga;
    }

    public void setHarga(String harga) {
        mHarga = harga;
    }


    public Integer getJumlah(){
        return mJumlah;
    }

    public void setJumlah(Integer jumlah) {

        mJumlah = jumlah;
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