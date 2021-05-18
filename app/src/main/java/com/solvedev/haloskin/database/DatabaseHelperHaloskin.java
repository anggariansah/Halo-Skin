package com.solvedev.haloskin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperHaloskin extends SQLiteOpenHelper {

    public DatabaseHelperHaloskin(Context context) {
        super(context, "db_haloskin", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tb_favorite(id_keranjang INTEGER PRIMARY KEY, id_produk TEXT, nama TEXT, " +
                "harga INTEGER, foto TEXT, deskripsi TEXT) ");
        db.execSQL("CREATE TABLE tb_keranjang(id INTEGER PRIMARY KEY, id_produk TEXT, nama TEXT, " +
                "harga INTEGER, jumlah INTEGER, foto TEXT) ");

        db.execSQL("CREATE TABLE tb_alamat(id INTEGER PRIMARY KEY, alamat TEXT, jenis TEXT, " +
                "id_kecamatan TEXT, id_kota TEXT, id_provinsi TEXT,lat TEXT, lng TEXT, catatan TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE tb_favorite");
            db.execSQL("DROP TABLE tb_keranjang");
            db.execSQL("DROP TABLE tb_alamat");
            db.execSQL("CREATE TABLE tb_favorite(id INTEGER PRIMARY KEY, id_produk TEXT) ");
            db.execSQL("CREATE TABLE tb_keranjang(id INTEGER PRIMARY KEY, id_produk TEXT) ");
            db.execSQL("CREATE TABLE tb_alamat(id INTEGER PRIMARY KEY, id_alamat TEXT) ");
        }
    }
}
