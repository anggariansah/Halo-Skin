package com.solvedev.haloskin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KonsulDetailResponse {

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
    private String data;

    @SerializedName("url")
    @Expose
    private Url url;

    @SerializedName("TokenDokter")
    private TokenDokter tokenDokter;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public TokenDokter getTokenDokter() {
        return tokenDokter;
    }

    public void setTokenDokter(TokenDokter tokenDokter) {
        this.tokenDokter = tokenDokter;
    }
}
