package com.example.psm;

import com.google.firebase.database.Exclude;

public class PesananDetails {

    private String mOrderID;
    private String mOrderDate;
    private String mOrderTime;
    private String mStatus;
    private String mNeedToPay;
    private String mKey;

    public PesananDetails() {
        //empty constructor needed
    }

    public PesananDetails(String OrderID, String OrderDate, String OrderTime, String Status, String NeedToPay) {

        mOrderID = OrderID;
        mOrderDate = OrderDate;
        mOrderTime = OrderTime;
        mStatus = Status;
        mNeedToPay = NeedToPay;

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

    public String getStatus() {

        return mStatus;
    }

    public void setStatus(String status) {

        mStatus = status;
    }

    public String getNeedToPay() {

        return mNeedToPay;
    }

    public void setNeedToPay(String needToPay) {

        mNeedToPay = needToPay;
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