package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiGantiActivity extends AppCompatActivity {


    private Toolbar tb;
    private ProgressDialog pg;
    PinView pvCode;
    Button btnVerify, btnKirimUlang;
    private TextView tvTimeLeft,tvKirim,tvTunggu;
    private long timeLeft = 120000;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_ganti);

        tb = findViewById(R.id.toolbar);
        pg = new ProgressDialog(this);
        pvCode = findViewById(R.id.pv_code);
        btnVerify = findViewById(R.id.btn_verifikasi);
        btnKirimUlang = findViewById(R.id.btn_kirim_ulang);
        tvTimeLeft = findViewById(R.id.tv_detik);
        tvTunggu = findViewById(R.id.tv_tunggu);
        tvKirim = findViewById(R.id.tv_kirim);

        Bundle extras = getIntent().getExtras();

        btnKirimUlang.setVisibility(View.GONE);

        final String email = extras.getString("email");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pvCode.getText().toString();

                if (code.isEmpty()){
                    Toast.makeText(VerifikasiGantiActivity.this,"Harap Masukkan Kode!!", Toast.LENGTH_LONG).show();
                }
                else {
                    verify(email,code);
                }
            }
        });

        btnKirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(email);
            }
        });


        sendCode(email);
        startTimer();

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                btnKirimUlang.setVisibility(View.VISIBLE);
                tvKirim.setVisibility(View.GONE);
                tvTunggu.setVisibility(View.GONE);
                tvTimeLeft.setVisibility(View.GONE);

            }
        }.start();

    }

    public void updateTimer(){
        int seconds = (int) timeLeft % 120000 / 1000;

        String timeLeftText;

        timeLeftText = "" + seconds;
        tvTimeLeft.setText(timeLeftText);

    }

    public  void verify(final String email, final String code){
        pg.setMessage("Harap Tunggu!!");
        pg.setCancelable(false);
        pg.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> verifyCode = api.verifyCode(email,code,Base.apiToken);
        verifyCode.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                pg.dismiss();

                if (response.body() != null){
                    if (!response.body().getError()){

                        Intent intent = new Intent(VerifikasiGantiActivity.this, KataSandiUlangActivity.class);
                        intent.putExtra("kode",code);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(VerifikasiGantiActivity.this, "Kode Verifikasi Salah!!", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(VerifikasiGantiActivity.this, "Erorr!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                pg.dismiss();
                Toast.makeText(VerifikasiGantiActivity.this, "Erorr!!"+t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void sendCode(final String email){
        pg.setMessage("Harap Tunggu!!");
        pg.setCancelable(false);
        pg.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> sendCode = api.sendCode(email, Base.apiToken);
        sendCode.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                pg.dismiss();

                if (response.body() != null){
                    if (!response.body().getError()) {


                    } else {
                        Toast.makeText(VerifikasiGantiActivity.this, "Jaringan bermasalah!!", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(VerifikasiGantiActivity.this, "Erorr!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                pg.dismiss();
                Toast.makeText(VerifikasiGantiActivity.this, "Erorr!!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}