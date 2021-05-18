package com.solvedev.haloskin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.solvedev.haloskin.R;

import java.util.Objects;

public class SignUp extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private String CAL2 = "cal2";
    private TextInputEditText edtTanggalLahir, edtNama, edtTempatLahir, edtKota, edtAlamat;
    private TextView tvValueJenisKelamin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btnSelanjutnya = findViewById(R.id.btn_selanjutnya);
        ImageView ivTanggal = findViewById(R.id.iv_tanggal);
        edtTanggalLahir = findViewById(R.id.edt_tanggal_lahir);
        edtNama = findViewById(R.id.edt_nama);
        edtTempatLahir = findViewById(R.id.edt_tempat_lahir);
        edtKota = findViewById(R.id.edt_kota);
        edtAlamat = findViewById(R.id.edt_alamat_rumah);
        RadioGroup rgJenisKelamin = findViewById(R.id.rg_jenis_kelamin);
        tvValueJenisKelamin = findViewById(R.id.tv_value_jenis_kelamin);

        tvValueJenisKelamin.setVisibility(View.GONE);
        edtTanggalLahir.setEnabled(false);




        rgJenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_laki:
                        tvValueJenisKelamin.setText("Laki-Laki");
                        break;
                    case R.id.radio_perempuan:
                        tvValueJenisKelamin.setText("Perempuan");
                        break;


                }
            }
        });


        btnSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.requireNonNull(edtAlamat.getText()).toString().isEmpty() &&!Objects.requireNonNull(edtKota.getText()).toString().isEmpty() && !Objects.requireNonNull(edtNama.getText()).toString().isEmpty() && !Objects.requireNonNull(edtTempatLahir.getText()).toString().isEmpty() && !Objects.requireNonNull(edtTanggalLahir.getText()).toString().isEmpty()) {
                    Intent intent = new Intent(SignUp.this, SignUpKedua.class);
                    intent.putExtra("nama", Objects.requireNonNull(edtNama.getText()).toString());
                    intent.putExtra("tanggal_lahir", Objects.requireNonNull(edtTanggalLahir.getText()).toString());
                    intent.putExtra("tempat_lahir", Objects.requireNonNull(edtTempatLahir.getText()).toString());
                    intent.putExtra("kota", Objects.requireNonNull(edtKota.getText()).toString());
                    intent.putExtra("alamat", Objects.requireNonNull(edtAlamat.getText()).toString());
                    intent.putExtra("jenis_kelamin", tvValueJenisKelamin.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Harap isi semua data!", Toast.LENGTH_LONG).show();
                }
            }
        });

        ivTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp1 = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(SignUp.this);
                cdp1.show(getSupportFragmentManager(), CAL2);
            }
        });


    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear,
                          int dayOfMonth) {
        assert dialog.getTag() != null;
        if (dialog.getTag().equals(CAL2)) {
            edtTanggalLahir.setText(getString(R.string.calendar, year, monthOfYear + 1, dayOfMonth));
        }
    }
}
