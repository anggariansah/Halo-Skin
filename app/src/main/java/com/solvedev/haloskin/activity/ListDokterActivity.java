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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.DokterAdapter;
import com.solvedev.haloskin.model.Dokter;
import com.solvedev.haloskin.model.DokterResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDokterActivity extends AppCompatActivity {

    private List<Dokter> listDokter = new ArrayList<>();
    private List<Dokter> listSearchDokter = new ArrayList<>();

    private RecyclerView mRvDokter;
    private SearchView svTitle;
    private Toolbar tb;
    private LinearLayout linearPromo;

    private DokterAdapter adapterDokter;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dokter);

        tb = findViewById(R.id.toolbar);
        mRvDokter = findViewById(R.id.rv_dokter);
        svTitle = findViewById(R.id.sv_title);
        linearPromo = findViewById(R.id.linear_promo);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chat Dengan Dokter");
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

                if(newText.equals("")){
                    getListDokter();
                }else{
                    getSearchDokter(newText);

                }
//                int textlength = newText.length();
//
//                listDokter.clear();
//
//                for (int i = 0; i < listSearchDokter.size(); i++) {
//                    if (textlength <= listSearchDokter.get(i).getNama().length()) {
//                        if (newText.equalsIgnoreCase((String) listSearchDokter.get(i).getNama().subSequence(0, textlength))) {
//                            Dokter data = new Dokter();
//                            data.setId(listSearchDokter.get(i).getId());
//                            data.setNama(listSearchDokter.get(i).getNama());
//                            data.setPengalaman(listSearchDokter.get(i).getPengalaman());
//                            data.setLike(listSearchDokter.get(i).getLike());
//                            data.setDislike(listSearchDokter.get(i).getDislike());
//                            data.setHarga(listSearchDokter.get(i).getHarga());
//                            data.setJenis_dokter(listSearchDokter.get(i).getJenis_dokter());
//
//                            listDokter.add(data);
//                        }
//                    }
//                }
//
//
//                setAdapter();

                return false;
            }
        });

        linearPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDokterActivity.this, PromoTersediaActivity.class);
                startActivity(intent);
            }
        });


        getListDokter();

    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_konsultasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_riwayat :

                Intent intent = new Intent(ListDokterActivity.this, RiwayatKonsulActivity.class);
                startActivity(intent);

                break;

            case android.R.id.home :

                finish();
                break;
        }
        return true;
    }

    private void getListDokter() {
        listDokter.clear();

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<DokterResponse> getData = api.getListDokter(preference.getEmail(), preference.getToken(), Base.apiToken);
        getData.enqueue(new Callback<DokterResponse>() {
            @Override
            public void onResponse(Call<DokterResponse> call, Response<DokterResponse> response) {

                if(response.body() != null){

                    listDokter = response.body().getData();

                    if(listDokter != null){
                        setAdapter();
                    }else {
                        Toast.makeText(ListDokterActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ListDokterActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<DokterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListDokterActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchDokter(String nama) {
        listDokter.clear();

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<DokterResponse> getData = api.getSearchDokter(preference.getEmail(), preference.getToken(), Base.apiToken, nama);
        getData.enqueue(new Callback<DokterResponse>() {
            @Override
            public void onResponse(Call<DokterResponse> call, Response<DokterResponse> response) {

                if(response.body() != null){

                    listDokter = response.body().getData();

                    if(listDokter != null){
                        setAdapter();
                    }else {
                        Toast.makeText(ListDokterActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ListDokterActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<DokterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListDokterActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mRvDokter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        mRvDokter.setHasFixedSize(true);
        adapterDokter = new DokterAdapter(ListDokterActivity.this, listDokter, new DokterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

            }
        }, new DokterAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View v, int posisition) {

                Dokter model = listDokter.get(posisition);

                Intent intent = new Intent(ListDokterActivity.this, DetailDokterKonsultasiActivity.class);
                intent.putExtra("id", model.getId());
                startActivity(intent);
            }
        });
        mRvDokter.setAdapter(adapterDokter);
        adapterDokter.notifyDataSetChanged();

    }
}
