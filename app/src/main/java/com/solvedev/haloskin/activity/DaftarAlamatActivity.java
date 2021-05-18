package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.DaftarAlamatAdapter;
import com.solvedev.haloskin.database.DatabaseHelperHaloskin;
import com.solvedev.haloskin.model.DaftarAlamat;

import java.util.ArrayList;

public class DaftarAlamatActivity extends AppCompatActivity {

    private RecyclerView mRvAlamat;
    DaftarAlamatAdapter adapterAlamat;
    SQLiteDatabase database;
    ArrayList<DaftarAlamat> listAlamat = new ArrayList<>();
    LinearLayout linearEmpty;

    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_alamat);

        mRvAlamat = findViewById(R.id.rv_daftar_alamat);
        linearEmpty = findViewById(R.id.linear_empty_state);
        tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar Alamat");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        getData();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_produk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_add :
                Intent intentFavorite = new Intent(DaftarAlamatActivity.this, AlamatPengirimanActivity.class);
                startActivity(intentFavorite);
                finish();

                break;

            case android.R.id.home :

                finish();
                break;

        }
        return true;
    }

    void getData(){
        database = new DatabaseHelperHaloskin(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM tb_alamat", null);

        if(cursor.moveToFirst()){
            do {

                DaftarAlamat alamat = new DaftarAlamat();
                alamat.setId(cursor.getInt(0));
                alamat.setAlamat(cursor.getString(1));
                alamat.setJenis_alamat(cursor.getString(2));
                alamat.setId_kecamatan(cursor.getString(3));
                alamat.setId_kota(cursor.getString(4));
                alamat.setId_provinsi(cursor.getString(5));
                alamat.setLat(cursor.getString(6));
                alamat.setLng(cursor.getString(7));
                alamat.setCatatan(cursor.getString(8));

                listAlamat.add(alamat);

            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        if(listAlamat.size() != 0){
            setAdapter();
            linearEmpty.setVisibility(View.GONE);
        }else{
            mRvAlamat.setVisibility(View.GONE);
            linearEmpty.setVisibility(View.VISIBLE);
        }
    }

    void delete(String id){

        database = new DatabaseHelperHaloskin(DaftarAlamatActivity.this).getWritableDatabase();

        long hapus =  database.delete("tb_alamat", "id=? ", new String[]{id});

        if(hapus != -1){
            Toast.makeText(this, "Hapus Sukses", Toast.LENGTH_SHORT).show();
            Intent intentFavorite = new Intent(DaftarAlamatActivity.this, DaftarAlamatActivity.class);
            startActivity(intentFavorite);
            finish();
        }else{
            Toast.makeText(this, "Hapus Gagal", Toast.LENGTH_SHORT).show();
        }

        database.close();

    }

    private void setAdapter() {
        mRvAlamat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        mRvAlamat.setHasFixedSize(true);
        adapterAlamat = new DaftarAlamatAdapter(DaftarAlamatActivity.this, listAlamat, new DaftarAlamatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                DaftarAlamat model = listAlamat.get(posisition);

                setResult(Activity.RESULT_OK, new Intent()
                        .putExtra("kode_kecamatan", model.getId_kecamatan())
                        .putExtra("kode_kota", model.getId_kota())
                        .putExtra("kode_provinsi", model.getId_provinsi())
                        .putExtra("alamat", model.getAlamat())
                        .putExtra("catatan", model.getCatatan()));

                finish();

            }
        }, new DaftarAlamatAdapter.OnEditButtonClickListener() {
            @Override
            public void onEditButtonClick(View v, int posisition) {

            }
        }, new DaftarAlamatAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(View v, int posisition) {

                DaftarAlamat model = listAlamat.get(posisition);

                delete(String.valueOf(model.getId()));

            }
        });
        mRvAlamat.setAdapter(adapterAlamat);
        adapterAlamat.notifyDataSetChanged();

    }


}