package com.solvedev.haloskin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.service.Common;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private UserPreferences preference;
    private TextView tvTimeLeft,tvKirim,tvTunggu;
    private long timeLeft = 120000;
    private CountDownTimer countDownTimer;

    String token;

    String email, password, code;

    Button btnVerify, btnKirimUlang;
    PinView pvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        pvCode = findViewById(R.id.pv_code);
        btnVerify = findViewById(R.id.btn_verifikasi);
        btnKirimUlang = findViewById(R.id.btn_kirim_ulang);
        tvTimeLeft = findViewById(R.id.tv_detik);
        tvTunggu = findViewById(R.id.tv_tunggu);
        tvKirim = findViewById(R.id.tv_kirim);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            password = extras.getString("password");
        }


        getCurrentFirebaseToken();

        btnKirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimUlangKode(email,password);

                startTimer();

                btnKirimUlang.setVisibility(View.GONE);
                tvKirim.setVisibility(View.VISIBLE);
                tvTunggu.setVisibility(View.VISIBLE);
                tvTimeLeft.setVisibility(View.VISIBLE);

            }
        });

        progressDialog = new ProgressDialog(VerifikasiActivity.this);
        preference = new UserPreferences(VerifikasiActivity.this);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = pvCode.getText().toString();

                verifikasiCode(email, code);


            }
        });

        startTimer();

        btnKirimUlang.setVisibility(View.GONE);

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

    private void verifikasiCode(final String email, final String verifyCode) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.verifiyCode(email, verifyCode, token, Base.apiToken);
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {

                        User data = response.body().getData();

                        preference.createLoginSessionUser(data.getEmail(), data.getToken());

                        Intent intent = new Intent(VerifikasiActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(VerifikasiActivity.this, "Verifikasi Kode Berhasil!!", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(VerifikasiActivity.this, "Kode yang anda masukan salah!!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(VerifikasiActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(VerifikasiActivity.this, "Network Error Verif " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void kirimUlangKode(final String email, final String password) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.login(email, password, "user_login","user", Base.apiToken);
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {



                    } else {
                        Toast.makeText(VerifikasiActivity.this, "Jaringan bermasalah!!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(VerifikasiActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(VerifikasiActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getCurrentFirebaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Log.e("currentToken", token);

                        Common.tokenNotif = token;

                        Log.d("TAG", ""+token);
                    }
                });
    }
}
