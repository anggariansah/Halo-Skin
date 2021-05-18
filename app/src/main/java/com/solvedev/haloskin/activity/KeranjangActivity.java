package com.solvedev.haloskin.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.KeranjangAdapter;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.RupiahConvert;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeranjangActivity extends AppCompatActivity {

    private List<Produk> listProduk = new ArrayList<>();
    private Integer[] arrayHarga;
    private RecyclerView mRvProduk;
    private TextView tvAlamat, tvEmptyState;

    private KeranjangAdapter adapterProduk;
    private ProgressDialog progressDialog;
    private Toolbar tb;

    private UserPreferences preference;

    private TextView tvUbah, tvTotalBelanja;
    private Button btnNext;
    private EditText edtCatatan;
    private FloatingActionButton fabTambah;

    private String kodeProvinsi, kodeKota, kodeKecamatan, alamat, catatan;
    private int totalBelanja = 0, totalHarga = 0;

    private RupiahConvert rupiahConvert = new RupiahConvert();

    private static final int PLACE_PICKER_REQUEST = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        mRvProduk = findViewById(R.id.rv_keranjang);
        tvUbah = findViewById(R.id.tv_ubah);
        fabTambah = findViewById(R.id.fab_tambah_keranjang);
        btnNext = findViewById(R.id.btn_next);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvTotalBelanja = findViewById(R.id.tv_total_belanja);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        edtCatatan = findViewById(R.id.edt_catatan);
        tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Keranjang");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);

        getListProduk();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kodeKecamatan == null || kodeKota == null || kodeProvinsi == null || alamat == null){
                    Toast.makeText(KeranjangActivity.this, "Harap Lengkapi Alamat", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(KeranjangActivity.this, DetailPembayaran.class);
                    intent.putExtra("kode_kecamatan", kodeKecamatan);
                    intent.putExtra("kode_kota", kodeKota);
                    intent.putExtra("kode_provinsi", kodeProvinsi);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("total_belanja", totalBelanja);
                    intent.putParcelableArrayListExtra("produk", (ArrayList<? extends Parcelable>) listProduk);
                    startActivity(intent);
                    finish();
                }

            }
        });

        tvUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(KeranjangActivity.this, DaftarAlamatActivity.class);
                startActivityForResult(a, PLACE_PICKER_REQUEST);
            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KeranjangActivity.this, BuyProdukActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            kodeKecamatan = data.getStringExtra("kode_kecamatan");
            kodeKota = data.getStringExtra("kode_kota");
            kodeProvinsi = data.getStringExtra("kode_provinsi");
            alamat = data.getStringExtra("alamat");
            catatan = data.getStringExtra("catatan");

            tvAlamat.setText(alamat);
            edtCatatan.setText(catatan);
        }
    }

    private void deleteCart(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.deleteCart(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()){

                        Toast.makeText(KeranjangActivity.this, "Berhasil Dihapus dari Keranjang!", Toast.LENGTH_SHORT).show();

                        getListProduk();

                    }else{
                        Toast.makeText(KeranjangActivity.this, "Gagal Menghapus dari Keranjang!", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(KeranjangActivity.this, "error null :"+response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(KeranjangActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void getListProduk() {

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> getData = api.getCart(preference.getEmail(), preference.getToken(), Base.apiToken);
        getData.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {

                totalBelanja = 0;

                if(response.body() != null){

                    listProduk = response.body().getData();

                    arrayHarga = new Integer[listProduk.size()];

                    if(listProduk.size() != 0){

                        for (int i = 0; i < listProduk.size(); i++){
                            totalBelanja = totalBelanja + listProduk.get(i).getHarga();
                        }

                        tvTotalBelanja.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalBelanja)));
                        setAdapter();

                        mRvProduk.setVisibility(View.VISIBLE);
                        tvEmptyState.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(KeranjangActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                        mRvProduk.setVisibility(View.GONE);
                        tvEmptyState.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(KeranjangActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                    mRvProduk.setVisibility(View.GONE);
                    tvEmptyState.setVisibility(View.VISIBLE);
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(KeranjangActivity.this, "Network Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                mRvProduk.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter() {
        mRvProduk.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        mRvProduk.setHasFixedSize(true);
        adapterProduk = new KeranjangAdapter(KeranjangActivity.this, listProduk, new KeranjangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {
                Produk model = listProduk.get(posisition);

            }
        }, new KeranjangAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View v, int posisition, int jumlah) {
                Produk model = listProduk.get(posisition);

                int totalBuy = 0;

                Log.d("totalNilai", ""+model.getJumlah());

                if(jumlah == 0){
                    deleteCart(String.valueOf(model.getId_barang()));
                }else{
                    for (int i = 0; i < listProduk.size(); i++){

                        arrayHarga[i] = listProduk.get(i).getHarga() * listProduk.get(i).getJumlah();

                        Log.d("nilaiHarga",""+listProduk.get(i).getHarga());
                        Log.d("nilaiJumlah",""+listProduk.get(i).getJumlah());
                        Log.d("nilaiArray",""+arrayHarga[i]);
                    }


                    for (int i = 0; i < arrayHarga.length; i++){
                        totalBuy = totalBuy + arrayHarga[i];
                        Log.d("nilaiTotal",""+totalBelanja);
                    }

                    totalBelanja = totalBuy;
                }

                tvTotalBelanja.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalBelanja)));

            }
        });
        mRvProduk.setAdapter(adapterProduk);
        adapterProduk.notifyDataSetChanged();

    }
}