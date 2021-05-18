package com.solvedev.haloskin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DokterResponse {

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
    private List<Dokter> data;

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

    public List<Dokter> getData() {
        return data;
    }

    public void setData(List<Dokter> data) {
        this.data = data;
    }
}
