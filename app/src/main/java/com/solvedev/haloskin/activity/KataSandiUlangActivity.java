package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KataSandiUlangActivity extends AppCompatActivity {

    private TextInputEditText edtPassword,edtKonfirmasiPassword;
    Button btnKonfirmasi;
    ProgressDialog pg;
    Toolbar tb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kata_sandi_ulang);

        tb = findViewById(R.id.toolbar);
        edtKonfirmasiPassword = findViewById(R.id.edt_konfirmasi_password);
        edtPassword = findViewById(R.id.edt_password);
        btnKonfirmasi = findViewById(R.id.btn_konfirmasi);
        pg = new ProgressDialog(this);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String code = extras.getString("kode");
        final String email = extras.getString("email");

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString().trim();
                String cPassword = edtKonfirmasiPassword.getText().toString().trim();

                if (password.equals(cPassword)){
                    gantiPassword(email,password,cPassword,code);
                }
                else {
                    Toast.makeText(KataSandiUlangActivity.this, "Password Tidak Benar!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

    }

    public void gantiPassword(final String email, final String password, final String cPassword, final String code){
        pg.setMessage("Harap Tunggu!!");
        pg.setCancelable(false);
        pg.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> updatePass = api.updatePassword(email,password,cPassword,code, Base.apiToken);
        updatePass.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                pg.dismiss();

                if (response.body() != null){
                    if (!response.body().getError()){

                        Intent intent = new Intent(KataSandiUlangActivity.this,LoginAccountActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(KataSandiUlangActivity.this, "Berhasil Mengganti Password!!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(KataSandiUlangActivity.this, "Jaringan Bermasalah!!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(KataSandiUlangActivity.this, "Error!!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                pg.dismiss();
                Toast.makeText(KataSandiUlangActivity.this, "Error!!"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}