package com.example.psm;

import com.google.firebase.database.Exclude;

public class Pesanan {

    private String mName;
    private String mHarga;
    private String mImageUrl;
    private Integer mJumlah;

    private String mOrderID;
    private String mOrderDate;
    private String mOrderTime;
    private String mKey;

    public Pesanan() {
        //empty constructor needed
    }

    public Pesanan(String Nama, String Harga, String Gambar, Integer Jumlah, String OrderID, String OrderDate, String OrderTime) {

        mName = Nama;
        mHarga = Harga;
        mImageUrl = Gambar;
        mJumlah = Jumlah;

        mOrderID = OrderID;
        mOrderDate = OrderDate;
        mOrderTime = OrderTime;


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

    public String getOrderID() {
        return mOrderID;
    }

    public void setOrderID(String orderID) {

        mOrderID = orderID;
    }

    public String getOrderDate() {

        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {

        mOrderDate = orderDate;
    }

    public String getOrderTime() {

        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {

        mOrderTime = orderTime;
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