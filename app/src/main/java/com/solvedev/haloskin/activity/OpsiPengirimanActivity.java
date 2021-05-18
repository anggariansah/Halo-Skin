package com.solvedev.haloskin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.OngkirAdapter;
import com.solvedev.haloskin.model.BiayaOngkirResponse;
import com.solvedev.haloskin.model.DataOngkir;
import com.solvedev.haloskin.model.Ongkir;
import com.solvedev.haloskin.model.OngkirResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpsiPengirimanActivity extends AppCompatActivity {

    private List<DataOngkir> listCost = new ArrayList<>();
    private String kodeKecamatan;
    private RecyclerView mRvCost;
    private OngkirAdapter adapterCost;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opsi_pengiriman);

        tb = findViewById(R.id.toolbar);
        mRvCost = findViewById(R.id.rv_cost);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            kodeKecamatan = extras.getString("kode_kecamatan");
        } else {
            kodeKecamatan = "501";
        }

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Opsi Pengiriman");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        progressDialog = new ProgressDialog(OpsiPengirimanActivity.this);

        tampilCost(kodeKecamatan);

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void tampilCost(String kodeKecamatan) {
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClientRajaOngkir().create(ApiRequest.class);
        Call<OngkirResponse> getData = api.getCost(Base.apiKeyRajaOngkir, "505", "subdistrict", kodeKecamatan, "subdistrict", "1000", "jne:pos:tiki:sicepat:jnt");
        getData.enqueue(new Callback<OngkirResponse>() {
            @Override
            public void onResponse(Call<OngkirResponse> call, Response<OngkirResponse> response) {

                if (response.body() != null) {

                    List<Ongkir> listOngkir = response.body().getRajaongkir().getResults();

                    for (Ongkir modelOngkir : listOngkir) {

                        List<BiayaOngkirResponse> listOngkirResponse = modelOngkir.getCosts();

                        for (BiayaOngkirResponse modelOngkirResponse : listOngkirResponse) {

                            DataOngkir model = new DataOngkir();

                            model.setCode(modelOngkir.getCode());
                            model.setService(modelOngkirResponse.getService());
                            model.setEtd(modelOngkirResponse.getCost().get(0).getEtd());
                            model.setValue(modelOngkirResponse.getCost().get(0).getValue());

                            listCost.add(model);

                            Log.d("Biaya", "" + modelOngkirResponse.getService());
                            Log.d("Biay", "" + modelOngkir.getName());
                            Log.d("Bia", "" + modelOngkirResponse.getCost().get(0).getEtd());
                            Log.d("Bia", "" + modelOngkirResponse.getCost().get(0).getValue());

                        }

                    }

                    if (listCost != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(OpsiPengirimanActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(OpsiPengirimanActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }


                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OngkirResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OpsiPengirimanActivity.this, "Network Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mRvCost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvCost.setHasFixedSize(true);
        adapterCost = new OngkirAdapter(OpsiPengirimanActivity.this, listCost, new OngkirAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                DataOngkir model = listCost.get(posisition);

                setResult(Activity.RESULT_OK, new Intent()
                        .putExtra("ekspedisi", model.getCode())
                        .putExtra("ongkir", String.valueOf(model.getValue())));

                finish();

            }
        });
        mRvCost.setAdapter(adapterCost);
        adapterCost.notifyDataSetChanged();

    }
}