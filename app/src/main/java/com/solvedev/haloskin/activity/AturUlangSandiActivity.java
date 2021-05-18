package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.solvedev.haloskin.R;

public class AturUlangSandiActivity extends AppCompatActivity {

    Toolbar tb;
    Button btnLanjut;
    private TextInputEditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_ulang_sandi);

        tb = findViewById(R.id.toolbar);
        edtEmail = findViewById(R.id.edt_email);
        btnLanjut = findViewById(R.id.btn_lanjut);

        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()){
                    Toast.makeText(AturUlangSandiActivity.this, "Harap Masukkan Email!!", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(AturUlangSandiActivity.this, VerifikasiGantiActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
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



    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}