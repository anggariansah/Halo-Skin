package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.CekPromoResponse;
import com.solvedev.haloskin.model.DetailDokterResponse;
import com.solvedev.haloskin.model.Dokter;
import com.solvedev.haloskin.model.KonsulDetailResponse;
import com.solvedev.haloskin.model.Promo;
import com.solvedev.haloskin.model.StartChatResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.RupiahConvert;
import com.solvedev.haloskin.utils.UserPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDokterKonsultasiActivity extends AppCompatActivity {


    TextView tvNama, tvJenisDokter, tvHarga, tvJumlahDiskon, tvTotalHarga;
    private Button btnBayar, btnPakai;
    private LinearLayout linearPromo;
    private EditText edtKodePromo;
    private TextView tvNamaPromo, tvJumlahPromo;
    private Toolbar tb;

    String nama, jenisDokter, id, dataKonsul, typePromo;
    int harga, totalHarga, jumlahPromo = 0, nominalPromo;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private RupiahConvert rupiahConvert = new RupiahConvert();
    StringBuilder stringItem = new StringBuilder();

    String jumlah = "1";

    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dokter_konsultasi);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }

        tvNama = findViewById(R.id.tv_nama);
        tvJenisDokter = findViewById(R.id.tv_jenis_dokter);
        tvHarga = findViewById(R.id.tv_harga);
        btnBayar = findViewById(R.id.btn_bayar);
        linearPromo = findViewById(R.id.linear_promo);
        tvNamaPromo = findViewById(R.id.tv_nama_promo);
        tvJumlahPromo = findViewById(R.id.tv_jumlah_promo);
        tvJumlahDiskon = findViewById(R.id.tv_jumlah_diskon);
        tvJumlahDiskon = findViewById(R.id.tv_jumlah_diskon);
        tvTotalHarga = findViewById(R.id.tv_total_harga);
        btnPakai = findViewById(R.id.btn_pakai);
        edtKodePromo = findViewById(R.id.edt_kode_promo);
        tb = findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(DetailDokterKonsultasiActivity.this);
        preference = new UserPreferences(DetailDokterKonsultasiActivity.this);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pembayaran");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }


        getDetailDokter();

        stringItem.append("{\"dokter\":[");
        stringItem.append("{\"dokter_id\":\"");
        stringItem.append(id);
        stringItem.append("\",\"jumlah\":");
        stringItem.append("\""+jumlah+"\"}");
        stringItem.append("]}");

        dataKonsul = stringItem.toString();

        Log.d("IDDOKTER",""+id);

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               transaksiKonsul(dataKonsul);
            }
        });

        btnPakai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kodePromo = edtKodePromo.getText().toString();

                checkPromo(kodePromo);
            }
        });
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void checkPromo(String kodePromo) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<CekPromoResponse> data = api.checkPromo(preference.getEmail(), preference.getToken(), Base.apiToken, kodePromo);
        data.enqueue(new Callback<CekPromoResponse>() {
            @Override
            public void onResponse(Call<CekPromoResponse> call, Response<CekPromoResponse> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {

                        Promo data = response.body().getData();

                        linearPromo.setVisibility(View.VISIBLE);
                        tvNamaPromo.setVisibility(View.VISIBLE);
                        tvJumlahPromo.setVisibility(View.VISIBLE);

                        nominalPromo = data.getNominal();
                        typePromo = data.getType();

                        if (typePromo.equals("precentage")){
                            jumlahPromo = harga * nominalPromo / 100;
                            totalHarga = harga - (harga * nominalPromo / 100);

                            Log.d("JUMLAHPROMO",""+jumlahPromo);
                            Log.d("JUMLAHPROMO",""+harga);
                            Log.d("JUMLAHPROMO",""+nominalPromo);
                        }

                        if (typePromo.equals("nominal")){
                            jumlahPromo = nominalPromo;
                            totalHarga = harga - nominalPromo;
                        }


                        tvNamaPromo.setText(data.getNama_promo());
                        tvJumlahPromo.setText("Kamu menghemat "+rupiahConvert.convertStringToRupiah(String.valueOf(jumlahPromo)));
                        tvTotalHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalHarga)));
                        tvJumlahDiskon.setText(rupiahConvert.convertStringToRupiah(String.valueOf(jumlahPromo)));

                        stringItem.setLength(0);

                        stringItem.append("{\"dokter\":[");
                        stringItem.append("{\"dokter_id\":\"");
                        stringItem.append(id);
                        stringItem.append("\",\"jumlah\":");
                        stringItem.append("\""+jumlah+"\"}");
                        stringItem.append("],\"id_promo\" : \""+data.getId()+"\"}");


                        dataKonsul = stringItem.toString();

                        Log.d("DATAKONSUL",""+dataKonsul);

                        Toast.makeText(DetailDokterKonsultasiActivity.this, "Kode Promo Berhasil Digunakan!!", Toast.LENGTH_SHORT).show();

                    } else {

                        linearPromo.setVisibility(View.GONE);
                        tvJumlahPromo.setText("Kode Promo Tidak Ditemukan!!");
                        tvJumlahPromo.setVisibility(View.VISIBLE);

                        Toast.makeText(DetailDokterKonsultasiActivity.this, "Kode Promo Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(DetailDokterKonsultasiActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CekPromoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailDokterKonsultasiActivity.this, "Network Error Promo " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showDialogTunggu() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_tunggu_konfirmasi, null);

        dialog = builder.create();


        dialog.setView(v);

        dialog.show();
    }

    private void transaksiKonsul(String dataTransaksi) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<KonsulDetailResponse> login = api.transaksiKonsul(preference.getEmail(), preference.getToken(), Base.apiToken, dataTransaksi);
        login.enqueue(new Callback<KonsulDetailResponse>() {
            @Override
            public void onResponse(Call<KonsulDetailResponse> call, Response<KonsulDetailResponse> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {

                        Toast.makeText(DetailDokterKonsultasiActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        if(response.body().getStatusCode() == 200){

                            String room_id =  response.body().getData();

                            String token_notif = response.body().getTokenDokter().getNotif_token();

                            setStartChat(room_id, token_notif);

                        }else if (response.body().getStatusCode() == 201){

                            Intent intent = new Intent(DetailDokterKonsultasiActivity.this, PembayaranKonsultasiActivity.class);
                            intent.putExtra("url", response.body().getUrl().getRedirect_url());
                            intent.putExtra("nama", nama);
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        Toast.makeText(DetailDokterKonsultasiActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(DetailDokterKonsultasiActivity.this, "error null"+response.body(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<KonsulDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailDokterKonsultasiActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDetailDokter() {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<DetailDokterResponse> getData = api.getDokterDetail(preference.getEmail(), preference.getToken(), Base.apiToken, id);
        getData.enqueue(new Callback<DetailDokterResponse>() {
            @Override
            public void onResponse(Call<DetailDokterResponse> call, Response<DetailDokterResponse> response) {

                if(response.body() != null){

                    if(!response.body().getError()){
                        Dokter model = response.body().getData();

                        totalHarga = model.getHarga();

                        harga = model.getHarga();

                        nama = model.getNama();

                        tvNama.setText(model.getNama());
                        tvJenisDokter.setText(model.getJenis_dokter());
                        tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getHarga())));
                        tvTotalHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalHarga)));
                        tvJumlahDiskon.setText(rupiahConvert.convertStringToRupiah(String.valueOf(jumlahPromo)));

                    }else{
                        Toast.makeText(DetailDokterKonsultasiActivity.this, "Tidak Ada Notes", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(DetailDokterKonsultasiActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<DetailDokterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailDokterKonsultasiActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStartChat(final String room_id, final String token_notif) {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<StartChatResponse> getData = api.startChat(preference.getEmail(), preference.getToken(), Base.apiToken, room_id);
        getData.enqueue(new Callback<StartChatResponse>() {
            @Override
            public void onResponse(Call<StartChatResponse> call, Response<StartChatResponse> response) {

                if(response.body() != null){

                    if(!response.body().getError()){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String date = sdf.format(new Date());

                        Intent intent = new Intent(DetailDokterKonsultasiActivity.this, ChatKonsultasiActiviy.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("room_id", room_id);
                        intent.putExtra("jenis", "chat");
                        intent.putExtra("tgl_transaksi", date);
                        intent.putExtra("notif_token", token_notif);
                        intent.putExtra("status_chat", "Y");
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(DetailDokterKonsultasiActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(DetailDokterKonsultasiActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<StartChatResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailDokterKonsultasiActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

