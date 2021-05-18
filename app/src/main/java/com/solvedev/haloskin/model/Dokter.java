package com.solvedev.haloskin.model;

import com.google.gson.annotations.SerializedName;

public class Dokter {

    private String id, pengalaman;
    private Integer like, dislike, harga;

    @SerializedName("nama_dokter")
    private String nama;

    @SerializedName("jenis_dokter_nama")
    private String jenis_dokter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenis_dokter() {
        return jenis_dokter;
    }

    public void setJenis_dokter(String jenis_dokter) {
        this.jenis_dokter = jenis_dokter;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPengalaman() {
        return pengalaman;
    }

    public void setPengalaman(String pengalaman) {
        this.pengalaman = pengalaman;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }
}
