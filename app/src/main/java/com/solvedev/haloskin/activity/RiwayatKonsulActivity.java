package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.RiwayatKonsulAdapter;
import com.solvedev.haloskin.model.NotesResponse;
import com.solvedev.haloskin.model.RiwayatKonsulResponse;
import com.solvedev.haloskin.model.RiwayatKonsultasi;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatKonsulActivity extends AppCompatActivity {

    Toolbar tb_konsul;
    RiwayatKonsulAdapter adapter;
    private RecyclerView rvKonsul;
    private TextView tvIsiNotes;

    private List<RiwayatKonsultasi> listRiwayat = new ArrayList<>();

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_konsul);

        tb_konsul = findViewById(R.id.tb_riwayat_konsul);
        rvKonsul = findViewById(R.id.rv_konsul);

        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);

        setSupportActionBar(tb_konsul);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow );
        }


        getListDokter();
    }


    private void getListDokter() {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);


        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<RiwayatKonsulResponse> getData = api.getRiwayatKonsul(preference.getEmail(), preference.getToken(), Base.apiToken);
        getData.enqueue(new Callback<RiwayatKonsulResponse>() {
            @Override
            public void onResponse(Call<RiwayatKonsulResponse> call, Response<RiwayatKonsulResponse> response) {

                if(response.body() != null){

                    listRiwayat = response.body().getData();

                    if(listRiwayat != null){
                        setAdapter();
                    }else {
                        Toast.makeText(RiwayatKonsulActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RiwayatKonsulActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<RiwayatKonsulResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RiwayatKonsulActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        rvKonsul.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) );
        rvKonsul.setHasFixedSize(true);
        adapter = new RiwayatKonsulAdapter(RiwayatKonsulActivity.this, listRiwayat, new RiwayatKonsulAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                RiwayatKonsultasi model = listRiwayat.get(posisition);

                Intent intent = new Intent(RiwayatKonsulActivity.this, ChatKonsultasiActiviy.class);
                intent.putExtra("nama", model.getList_item().get(0).getName());
                intent.putExtra("room_id", model.getRoom_id());
                intent.putExtra("notif_token", model.getNotif_token_dokter());
                intent.putExtra("jenis", "riwayat");
                intent.putExtra("tgl_transaksi", model.getTgl());
                intent.putExtra("status_chat", model.getStatus_chat());
                startActivity(intent);
                finish();

                Log.d("STATUSCHAT", ""+model.getStatus_chat());

            }
        }, new RiwayatKonsulAdapter.OnNotesClickListener() {
            @Override
            public void onNotesClick(View v, int posisition) {

                RiwayatKonsultasi model = listRiwayat.get(posisition);


                showAttachDialog(model.getRoom_id());

            }
        },new RiwayatKonsulAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View v, int posisition) {

                RiwayatKonsultasi model = listRiwayat.get(posisition);

                String jenis = "Beautician";
                int harga = 0;

                Intent intent = new Intent(RiwayatKonsulActivity.this, DetailDokterKonsultasiActivity.class);
                intent.putExtra("id", model.getId_dokter());
                startActivity(intent);

            }
        });
        rvKonsul.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void showAttachDialog(String room_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_notes, null);

        final AlertDialog dialog = builder.create();

        tvIsiNotes = v.findViewById(R.id.tv_notes);

        getNotes(room_id);

        dialog.setView(v);

        dialog.show();
    }


    private void getNotes(String room_id) {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<NotesResponse> getData = api.getNotes(preference.getEmail(), preference.getToken(), Base.apiToken, room_id);
        getData.enqueue(new Callback<NotesResponse>() {
            @Override
            public void onResponse(Call<NotesResponse> call, Response<NotesResponse> response) {

                if(response.body() != null){

                    if(!response.body().getError()){
                        String notes = response.body().getNotes();

                        tvIsiNotes.setText(notes);

                    }else{
                        Toast.makeText(RiwayatKonsulActivity.this, "Tidak Ada Notes", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RiwayatKonsulActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<NotesResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RiwayatKonsulActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}