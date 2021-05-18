package com.solvedev.haloskin.model;

import android.graphics.drawable.Drawable;

public class ListMenu {

    private Drawable foto;
    private String judul;

    public ListMenu(Drawable foto, String judul) {
        this.foto = foto;
        this.judul = judul;
    }

    public Drawable getFoto() {
        return foto;
    }

    public void setFoto(Drawable foto) {
        this.foto = foto;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
