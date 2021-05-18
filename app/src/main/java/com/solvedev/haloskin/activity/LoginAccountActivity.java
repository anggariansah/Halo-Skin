package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAccountActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvDaftar, tvLupa;
    private TextInputEditText edtEmail, edtPassword;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);

        btnLogin = findViewById(R.id.btn_login);
        tvDaftar = findViewById(R.id.tv_daftar);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        tvLupa = findViewById(R.id.tv_lupa_sandi);

        progressDialog = new ProgressDialog(LoginAccountActivity.this);
        preference = new UserPreferences(LoginAccountActivity.this);


        tvLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAccountActivity.this, AturUlangSandiActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String email = edtEmail.getText().toString();
               String password = edtPassword.getText().toString();

               loginAccount(email, password);
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAccountActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void loginAccount(final String email, final String password) {
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

                        Intent intent = new Intent(LoginAccountActivity.this, VerifikasiActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();

                        Toast.makeText(LoginAccountActivity.this, "Login Berhasil!!", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LoginAccountActivity.this, "Email atau Password Salah!!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(LoginAccountActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginAccountActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
