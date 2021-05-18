package com.solvedev.haloskin.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.KategoriTypeAdapter;
import com.solvedev.haloskin.model.Kategori;
import com.solvedev.haloskin.model.KategoriResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProdukActivity extends AppCompatActivity {

    SearchView svTitle;
    Toolbar tb;
    List<Kategori> listSearchKategori = new ArrayList<>();
    KategoriTypeAdapter adapterKategori;

    private List<Kategori> listKategori;
    private RecyclerView mRvKategori;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_produk);

        tb = findViewById(R.id.toolbar);
        mRvKategori = findViewById(R.id.rv_kategori);
        svTitle = findViewById(R.id.sv_title);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Beli Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }


        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);

        svTitle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                int textlength = newText.length();

                listKategori.clear();

                for (int i = 0; i < listSearchKategori.size(); i++) {
                    if (textlength <= listSearchKategori.get(i).getNama_tipe().length()) {
                        if (newText.equalsIgnoreCase((String) listSearchKategori.get(i).getNama_tipe().subSequence(0, textlength))) {
                            Kategori data = new Kategori();
                            data.setId(listSearchKategori.get(i).getId());
                            data.setNama_tipe(listSearchKategori.get(i).getNama_tipe());
                            data.setType(listSearchKategori.get(i).getType());

                            listKategori.add(data);
                        }
                    }
                }


                setAdapter();

                return false;
            }
        });

        getListKategori();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_produk, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_favorite:
                Intent intentFavorite = new Intent(BuyProdukActivity.this, FavoriteProductActivity.class);
                startActivity(intentFavorite);

                break;

            case R.id.menu_keranjang:
                Intent intentKeranjang = new Intent(BuyProdukActivity.this, KeranjangActivity.class);
                startActivity(intentKeranjang);

                break;

            case R.id.menu_history:
                Intent intenHistory = new Intent(BuyProdukActivity.this, RiwayatPembelianActivity.class);
                startActivity(intenHistory);

                break;

            case android.R.id.home:

                finish();
                break;

        }
        return true;
    }



    private void getListKategori() {

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<KategoriResponse> getData = api.getTypeList(preference.getEmail(), preference.getToken(), Base.apiToken);
        getData.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {

                if (response.body() != null) {

                    listKategori = response.body().getData();
                    listSearchKategori.addAll(listKategori);

                    if (listKategori != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(BuyProdukActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BuyProdukActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BuyProdukActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mRvKategori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvKategori.setHasFixedSize(true);
        adapterKategori = new KategoriTypeAdapter(BuyProdukActivity.this, listKategori, new KategoriTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {
                Kategori model = listKategori.get(posisition);

                Intent intent = new Intent(BuyProdukActivity.this, JenisListActivity.class);
                intent.putExtra("id_type", model.getId());
                startActivity(intent);

            }
        });
        mRvKategori.setAdapter(adapterKategori);
        adapterKategori.notifyDataSetChanged();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
