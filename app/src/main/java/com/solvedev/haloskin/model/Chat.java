package com.solvedev.haloskin.model;

import com.google.gson.annotations.SerializedName;

public class Chat {
    private String id, nama, foto, namaProduk ,fotoProduk, perhari, waktu;
    private Integer id_produk, qty;

    @SerializedName("sender")
    private String sender;

    @SerializedName("type")
    private String jenis;

    @SerializedName("time")
    private String tgl;

    @SerializedName("message")
    private String pesan;

    private ChatProduk produk;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getFotoProduk() {
        return fotoProduk;
    }

    public void setFotoProduk(String fotoProduk) {
        this.fotoProduk = fotoProduk;
    }

    public Integer getId_produk() {
        return id_produk;
    }

    public void setId_produk(Integer id_produk) {
        this.id_produk = id_produk;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPerhari() {
        return perhari;
    }

    public void setPerhari(String perhari) {
        this.perhari = perhari;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ChatProduk getProduk() {
        return produk;
    }

    public void setProduk(ChatProduk produk) {
        this.produk = produk;
    }
}

