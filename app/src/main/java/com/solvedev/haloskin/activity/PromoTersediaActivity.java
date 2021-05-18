package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.PromoAdapter;
import com.solvedev.haloskin.model.Promo;
import com.solvedev.haloskin.model.PromoResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoTersediaActivity extends AppCompatActivity {

    private List<Promo> listPromo = new ArrayList<>();
    private RecyclerView mRvPromo;

    private PromoAdapter adapterPromo;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_tersedia);

        tb = findViewById(R.id.toolbar);
        mRvPromo = findViewById(R.id.rv_promo);

        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);


        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Promo Tersedia");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        getListPromo();

    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getListPromo() {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<PromoResponse> getData = api.getPromoList(preference.getEmail(), preference.getToken(), Base.apiToken);
        getData.enqueue(new Callback<PromoResponse>() {
            @Override
            public void onResponse(Call<PromoResponse> call, Response<PromoResponse> response) {

                if(response.body() != null){

                    listPromo = response.body().getData();

                    if(listPromo != null){
                        setAdapter();
                    }else {
                        Toast.makeText(PromoTersediaActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(PromoTersediaActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<PromoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PromoTersediaActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mRvPromo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        mRvPromo.setHasFixedSize(true);
        adapterPromo = new PromoAdapter(PromoTersediaActivity.this, listPromo, new PromoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

            }
        });
        mRvPromo.setAdapter(adapterPromo);
        adapterPromo.notifyDataSetChanged();

    }
}