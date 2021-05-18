package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpKedua extends AppCompatActivity {

    private TextInputEditText edtNomor, edtEmail, edtPassword, edtKonfirmPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_kedua);

        Button btnDaftar = findViewById(R.id.btn_daftar);

        edtEmail = findViewById(R.id.edt_email);
        edtNomor = findViewById(R.id.edt_nomor);
        edtPassword = findViewById(R.id.edt_password);
        edtKonfirmPassword = findViewById(R.id.edt_konfirmasi_password);
        progressDialog = new ProgressDialog(this);

        final String nama = getIntent().getStringExtra("nama");
        final String tempatlahir = getIntent().getStringExtra("tempat_lahir");
        final String tanggallahir = getIntent().getStringExtra("tanggal_lahir");
        final String jeniskelamin = getIntent().getStringExtra("jenis_kelamin");
        final String kota = getIntent().getStringExtra("kota");
        final String alamat = getIntent().getStringExtra("alamat");


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent  intent = new Intent(SignUpKedua.this,VerifikasiActivity.class);
//                startActivity(intent);
//

                if (edtPassword.toString().isEmpty() || edtEmail.toString().isEmpty() || edtKonfirmPassword.toString().isEmpty() || edtNomor.toString().isEmpty()) {
                    Toast.makeText(SignUpKedua.this, "Tolong isikan semua data", Toast.LENGTH_LONG).show();
                } else {
                    String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();
                    String konfirmpass = Objects.requireNonNull(edtKonfirmPassword.getText()).toString();
                    if (konfirmpass.equals(password)) {


                        String pass = edtPassword.getText().toString();
                        String email = Objects.requireNonNull(edtEmail.getText()).toString();
                        String nomor = Objects.requireNonNull(edtNomor.getText()).toString();

                        register(email, pass, nama, tanggallahir, tempatlahir, jeniskelamin, kota, alamat, nomor);

                    } else {
                        Toast.makeText(SignUpKedua.this, "Password tidak sesuai", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    public void register(final String email, final String password, final String nama, final String tanggal_lahir, final String tempat_lahir, final String jenis_kelamin, final String kota, final String alamat, final String nomor_wa) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> register = api.register(email, password, nama, tanggal_lahir, tempat_lahir, jenis_kelamin, kota, alamat, nomor_wa, "user");
        register.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    boolean error = response.body().getError();

                    if (!error) {

                        Intent intent = new Intent(SignUpKedua.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(SignUpKedua.this, "Register Berhasil!!", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(SignUpKedua.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpKedua.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpKedua.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
