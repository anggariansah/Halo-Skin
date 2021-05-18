package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.KategoriJenisAdapter;
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

public class JenisListActivity extends AppCompatActivity {

    private List<Kategori> listKategori;
    private List<Kategori> listSearchKategori = new ArrayList<>();

    private RecyclerView mRvKategori;
    private SearchView svTitle;

    private KategoriJenisAdapter adapterKategori;

    private ProgressDialog progressDialog;
    private UserPreferences preference;
    private Toolbar tb;

    private Integer idType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_list);

        mRvKategori = findViewById(R.id.rv_kategori);
        svTitle = findViewById(R.id.sv_title);
        tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Beli Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idType = extras.getInt("id_type");
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
                    if (textlength <= listSearchKategori.get(i).getNama_jenis().length()) {
                        if (newText.equalsIgnoreCase((String) listSearchKategori.get(i).getNama_jenis().subSequence(0, textlength))) {
                            Kategori data = new Kategori();
                            data.setId(listSearchKategori.get(i).getId());
                            data.setNama_jenis(listSearchKategori.get(i).getNama_jenis());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_favorite :
                Intent intentFavorite = new Intent(JenisListActivity.this, FavoriteProductActivity.class);
                startActivity(intentFavorite);

                break;

            case R.id.menu_keranjang :
                Intent intentKeranjang = new Intent(JenisListActivity.this, KeranjangActivity.class);
                startActivity(intentKeranjang);

                break;

            case R.id.menu_riwayat :
                Intent intentRiwayat = new Intent(JenisListActivity.this, RiwayatPembelianActivity.class);
                startActivity(intentRiwayat);

                break;

            case android.R.id.home :

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
        Call<KategoriResponse> getData = api.getJenisList(preference.getEmail(), preference.getToken(), Base.apiToken, idType);
        getData.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {

                if(response.body() != null){

                    listKategori = response.body().getData();
                    listSearchKategori.addAll(listKategori);

                    if(listKategori != null){
                        setAdapter();
                    }else {
                        Toast.makeText(JenisListActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(JenisListActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(JenisListActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mRvKategori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        mRvKategori.setHasFixedSize(true);
        adapterKategori = new KategoriJenisAdapter(JenisListActivity.this, listKategori, new KategoriJenisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                Kategori model = listKategori.get(posisition);

                Intent intent = new Intent(JenisListActivity.this, ListProdukActivity.class);
                intent.putExtra("id_type",idType);
                intent.putExtra("id_jenis",model.getId());
                startActivity(intent);
            }
        });
        mRvKategori.setAdapter(adapterKategori);
        adapterKategori.notifyDataSetChanged();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
