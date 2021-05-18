package com.solvedev.haloskin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaksi {
    private Integer id, idtransaksi, diskon, jumlah, ongkir;
    private String no_invoice, id_user, method, status, no_resi;
    private String name, quantity;
    private Integer price;
    private List<ItemTransaksi> list_item;

    @SerializedName("created_at")
    private String tgl_transaksi;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo_invoice() {
        return no_invoice;
    }

    public void setNo_invoice(String no_invoice) {
        this.no_invoice = no_invoice;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public Integer getDiskon() {
        return diskon;
    }

    public void setDiskon(Integer diskon) {
        this.diskon = diskon;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo_resi() {
        return no_resi;
    }

    public void setNo_resi(String no_resi) {
        this.no_resi = no_resi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public List<ItemTransaksi> getList_item() {
        return list_item;
    }

    public void setList_item(List<ItemTransaksi> list_item) {
        this.list_item = list_item;
    }

    public Integer getOngkir() {
        return ongkir;
    }

    public void setOngkir(Integer ongkir) {
        this.ongkir = ongkir;
    }

    public Integer getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(Integer idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }
}
