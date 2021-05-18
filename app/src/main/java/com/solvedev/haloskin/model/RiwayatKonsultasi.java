package com.solvedev.haloskin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RiwayatKonsultasi {

    private String  foto, jenis_dokter, id_dokter, notif_token_dokter, status_chat;

    @SerializedName(value = "room_id", alternate = "no_invoice")
    private String room_id;

    @SerializedName("nama_dokter")
    private String nama;

    @SerializedName("tgl_transaksi")
    private String tgl;

    private List<Item> list_item;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJenis_dokter() {
        return jenis_dokter;
    }

    public void setJenis_dokter(String jenis_dokter) {
        this.jenis_dokter = jenis_dokter;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(String id_dokter) {
        this.id_dokter = id_dokter;
    }

    public List<Item> getList_item() {
        return list_item;
    }

    public void setList_item(List<Item> list_item) {
        this.list_item = list_item;
    }

    public String getNotif_token_dokter() {
        return notif_token_dokter;
    }

    public void setNotif_token_dokter(String notif_token_dokter) {
        this.notif_token_dokter = notif_token_dokter;
    }

    public String getStatus_chat() {
        return status_chat;
    }

    public void setStatus_chat(String status_chat) {
        this.status_chat = status_chat;
    }
}
