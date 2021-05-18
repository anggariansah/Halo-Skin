package com.solvedev.haloskin.model;

import com.google.gson.annotations.SerializedName;

public class NotificationResponse {

    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("notification")
    private Notification notification;

    @SerializedName("data")
    private DataModel data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }
}
