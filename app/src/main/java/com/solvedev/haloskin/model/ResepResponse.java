package com.solvedev.haloskin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResepResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("Error")
    @Expose
    private Boolean error;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private List<Resep> data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Resep> getData() {
        return data;
    }

    public void setData(List<Resep> data) {
        this.data = data;
    }
}
