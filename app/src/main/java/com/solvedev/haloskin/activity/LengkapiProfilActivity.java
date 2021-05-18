package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import android.annotation.SuppressLint;
import android.widget.RelativeLayout;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LengkapiProfilActivity extends AppCompatActivity implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    ArrayList<String> keluhan = new ArrayList<String>();

    Button btnInformasiDasar, btnInformasiKontak, btnInformasiPersonal, btnSimpan;
    LinearLayout linearInformasiDasar, linearInformasiKontak, linearInformasiPersonal;
    TextInputEditText edtNama, edtTempatLahir, edtTglLahir, edtKota, edtAlamat, edtNomor, edtEmail;

    private UserPreferences preference;
    private ProgressDialog progressDialog;
    private RadioGroup rgJenisKelamin, rgHamil, rgKulitNormal, rgKulitBerminyak, rgKulitKering, rgKulitKombinasi, rgSensitif, rgJerawat, rgTingkatJerawatTidakAda, rgTingkatJerawatRingan, rgTingkatJerawatSedang, rgTingkatJerawatAkut,rgIritasi;
    private TextView tvValueJenisKelamin, tvValueUmur, tvValueHamil, tvValueJenisKulit, tvValueSensitif, tvValueJerawat, tvValueTingkatJerawat, tvValueKeluhan,tvValueKeluhanLain,tvValueMinum,tvValueSayur,tvValueMinyak, tvValueIritasi;
    private CompoundButtonGroup rgUmur, ckKeluhan, rgKeluhanLain,rgMinum,rgSayur,rgMinyak;
    private Toolbar tb;
    private String CAL2 = "cal2";
    private RelativeLayout rlTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_profil);

        btnInformasiDasar = findViewById(R.id.btn_informasi_dasar);
        btnInformasiKontak = findViewById(R.id.btn_informasi_kontak);
        btnInformasiPersonal = findViewById(R.id.btn_informasi_personal);
        edtNama = findViewById(R.id.edt_nama);
        edtTempatLahir = findViewById(R.id.edt_tempat_lahir);
        edtTglLahir = findViewById(R.id.edt_tanggal_lahir);
        edtKota = findViewById(R.id.edt_kota);
        edtAlamat = findViewById(R.id.edt_alamat_rumah);
        edtNomor = findViewById(R.id.edt_nomor);
        edtEmail = findViewById(R.id.edt_email);
        btnSimpan = findViewById(R.id.btn_simpan);
        linearInformasiDasar = findViewById(R.id.linear_informasi_dasar);
        linearInformasiKontak = findViewById(R.id.linear_informasi_kontak);
        linearInformasiPersonal = findViewById(R.id.linear_informasi_personal);
        tvValueJenisKelamin = findViewById(R.id.tv_value_jenis_kelamin);
        rgJenisKelamin = findViewById(R.id.rg_jenis_kelamin);
        rgUmur = findViewById(R.id.rg_umur);
        tvValueUmur = findViewById(R.id.tv_value_umur);
        tvValueHamil = findViewById(R.id.tv_value_hamil);
        rgHamil = findViewById(R.id.rg_hamil);
        tvValueJenisKulit = findViewById(R.id.tv_value_jenis_kulit);
        rgKulitNormal = findViewById(R.id.rg_kulit_normal);
//        rgKulitBerminyak = findViewById(R.id.rg_kulit_berminyak);
//        rgKulitKering = findViewById(R.id.rg_kulit_kering);
//        rgKulitKombinasi = findViewById(R.id.rg_kulit_kombinasi);
        tvValueSensitif = findViewById(R.id.tv_value_sensitif);
        rgSensitif = findViewById(R.id.rg_sensitif);

//        rgTingkatJerawatSedang = findViewById(R.id.rg_tingkat_jerawat_sedang);
//        rgTingkatJerawatAkut = findViewById(R.id.rg_tingkat_jerawat_akut);
        tvValueJerawat = findViewById(R.id.tv_value_jerawat);
        tvValueTingkatJerawat = findViewById(R.id.tv_value_tingkat_jerawat);
        tvValueKeluhan = findViewById(R.id.tv_value_keluhan);
        ckKeluhan = findViewById(R.id.ck_keluhan);
        rgKeluhanLain = findViewById(R.id.rg_keluhan_lain);
        tvValueKeluhanLain = findViewById(R.id.tv_value_keluhan_lain);
        tvValueMinum = findViewById(R.id.tv_value_minum);
        rgMinum = findViewById(R.id.rg_minum);
        rgJerawat = findViewById(R.id.rg_jerawat);
        rgSayur = findViewById(R.id.rg_sayur);
        tvValueSayur = findViewById(R.id.tv_value_sayur);
        tvValueMinyak = findViewById(R.id.tv_value_minyak);
        rgMinyak = findViewById(R.id.rg_makan_berminyak);
        tvValueIritasi = findViewById(R.id.tv_value_iritasi);
        rgIritasi = findViewById(R.id.rg_iritasi);

        progressDialog = new ProgressDialog(LengkapiProfilActivity.this);
        preference = new UserPreferences(LengkapiProfilActivity.this);

        tvValueJenisKelamin.setVisibility(View.GONE);
        tvValueUmur.setVisibility(View.GONE);
        tvValueHamil.setVisibility(View.GONE);
        tvValueJerawat.setVisibility(View.GONE);
        tvValueJenisKulit.setVisibility(View.GONE);
        tvValueTingkatJerawat.setVisibility(View.GONE);
        tvValueSensitif.setVisibility(View.GONE);
        tvValueKeluhan.setVisibility(View.GONE);
        tvValueKeluhanLain.setVisibility(View.GONE);
        tvValueMinum.setVisibility(View.GONE);
        tvValueSayur.setVisibility(View.GONE);
        tvValueMinyak.setVisibility(View.GONE);
        tvValueIritasi.setVisibility(View.GONE);
        tb = findViewById(R.id.tb_lengkapi);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        rgJenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_laki:
                        tvValueJenisKelamin.setText("Laki-Laki");
                      //  Toast.makeText(LengkapiProfilActivity.this, "Laki- Laki", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio_perempuan:
                        tvValueJenisKelamin.setText("Perempuan");
                      //  Toast.makeText(LengkapiProfilActivity.this, "Perempuan", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        rgIritasi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_iritasi:
                        tvValueIritasi.setText("Y");
                      //  Toast.makeText(LengkapiProfilActivity.this, "Ya", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio_tidak_iritasi:
                        tvValueIritasi.setText("N");
                      //  Toast.makeText(LengkapiProfilActivity.this, "Tidak", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        rgKulitNormal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_kulit_normal:
                      //  Toast.makeText(LengkapiProfilActivity.this, "Normal", Toast.LENGTH_LONG).show();
                        tvValueJenisKulit.setText("normal");
                        break;
                }
            }
        });

//        rgKulitBerminyak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_kulit_berminyak:
//                        Toast.makeText(LengkapiProfilActivity.this, "Berminyak", Toast.LENGTH_LONG).show();
//                        tvValueJenisKulit.setText("berminyak");
//                        break;
//                }
//            }
//        });
//
//        rgKulitKering.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_kulit_kering:
//                        Toast.makeText(LengkapiProfilActivity.this, "Kering", Toast.LENGTH_LONG).show();
//                        tvValueJenisKulit.setText("kering");
//                        break;
//                }
//            }
//        });
//
//        rgKulitKombinasi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_kulit_Kombinasi:
//                        Toast.makeText(LengkapiProfilActivity.this, "Kombinasi", Toast.LENGTH_LONG).show();
//                        tvValueJenisKulit.setText("kombinasi");
//                        break;
//                }
//            }
//        });

        rgHamil.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_hamil:
                        tvValueHamil.setText("Y");
                     //   Toast.makeText(LengkapiProfilActivity.this, "Ya", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio_tidak_hamil:
                        tvValueHamil.setText("N");
                     //   Toast.makeText(LengkapiProfilActivity.this, "Tidak", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        rgUmur.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
              //  Toast.makeText(LengkapiProfilActivity.this, value, Toast.LENGTH_LONG).show();
                tvValueUmur.setText(value);
            }
        });

        rgSensitif.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_sensitif:
                     //   Toast.makeText(LengkapiProfilActivity.this, "Ya", Toast.LENGTH_LONG).show();
                        tvValueSensitif.setText("Y");
                        break;
                    case R.id.radio_tidak_sensitif:
                     //   Toast.makeText(LengkapiProfilActivity.this, "Tidak", Toast.LENGTH_LONG).show();
                        tvValueSensitif.setText("N");
                        break;
                }
            }
        });

        rgJerawat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_jerawat:
                      //  Toast.makeText(LengkapiProfilActivity.this, "Ya", Toast.LENGTH_LONG).show();
                        tvValueJerawat.setText("Y");
                        break;
                    case R.id.radio_tidak_sensitif:
                     //   Toast.makeText(LengkapiProfilActivity.this, "Tidak", Toast.LENGTH_LONG).show();
                        tvValueJerawat.setText("N");
                        break;
                }
            }
        });

        rgTingkatJerawatTidakAda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_tingkat_tidak_ada:
                     //   Toast.makeText(LengkapiProfilActivity.this, "Tidak Ada", Toast.LENGTH_LONG).show();
                        tvValueTingkatJerawat.setText("tidak_ada");
                        break;
                }
            }
        });


        rgTingkatJerawatRingan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_jerawat_ringan:
                      //  Toast.makeText(LengkapiProfilActivity.this, "Ringan", Toast.LENGTH_LONG).show();
                        tvValueTingkatJerawat.setText("ringan");
                        break;
                }
            }
        });


        rgTingkatJerawatSedang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_jerawat_ringan:
                      //  Toast.makeText(LengkapiProfilActivity.this, "Sedang", Toast.LENGTH_LONG).show();
                        tvValueTingkatJerawat.setText("sedang");
                        break;
                }
            }
        });


        rgTingkatJerawatAkut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_jerawat_akut:
                      //  Toast.makeText(LengkapiProfilActivity.this, "Akut", Toast.LENGTH_LONG).show();
                        tvValueTingkatJerawat.setText("meradang");
                        break;
                }
            }
        });

        ckKeluhan.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {

                if (isChecked) {
                    keluhan.add(value);
                    String final_keluhan = "";
                    for (String Keluhans : keluhan) {
                        final_keluhan = final_keluhan + Keluhans + ",";

                    }
                    tvValueKeluhan.setText(final_keluhan);
                }


            }
        });

        rgKeluhanLain.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
                tvValueKeluhanLain.setText(value);
              //  Toast.makeText(LengkapiProfilActivity.this,value,Toast.LENGTH_LONG).show();
            }
        });

        rgMinum.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
                tvValueMinum.setText(value);
               // Toast.makeText(LengkapiProfilActivity.this,value,Toast.LENGTH_LONG).show();
            }
        });

        rgSayur.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
                if (value.equals("Setiap Hari")){
                    tvValueSayur.setText("setiap_hari");
              //      Toast.makeText(LengkapiProfilActivity.this, "setiap_hari", Toast.LENGTH_LONG).show();
                }
                 if (value.equals("1x Seminggu")){
                    tvValueSayur.setText("seminggu_1");
               //     Toast.makeText(LengkapiProfilActivity.this, "seminggu_1", Toast.LENGTH_LONG).show();
                }
                else if (value.equals("2-3x Seminggu")){
                    tvValueSayur.setText("seminggu_2_3");
               //     Toast.makeText(LengkapiProfilActivity.this, "seminggu_2_3", Toast.LENGTH_LONG).show();
                }
                else if (value.equals("4-5x Seminggu")){
                    tvValueSayur.setText("seminggu_4_5");
               //     Toast.makeText(LengkapiProfilActivity.this, "seminggu_4_5", Toast.LENGTH_LONG).show();
                }

//                tvValueSayur.setText(value);
//                Toast.makeText(LengkapiProfilActivity.this, value, Toast.LENGTH_LONG).show();
            }
        });

        rgMinyak.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
                if (value.equals("Setiap Hari")){
                    tvValueSayur.setText("setiap_hari");
                //    Toast.makeText(LengkapiProfilActivity.this, "setiap_hari", Toast.LENGTH_LONG).show();
                }
                if (value.equals("1x Seminggu")){
                    tvValueSayur.setText("seminggu_1");
               //     Toast.makeText(LengkapiProfilActivity.this, "seminggu_1", Toast.LENGTH_LONG).show();
                }
                else if (value.equals("2-4x Seminggu")){
                    tvValueSayur.setText("seminggu_2_4");
              //      Toast.makeText(LengkapiProfilActivity.this, "seminggu_2_4", Toast.LENGTH_LONG).show();
                }
                else if (value.equals("1x Dalam 2 minggu")){
                    tvValueSayur.setText("duaminggu_1");
               //     Toast.makeText(LengkapiProfilActivity.this, "duaminggu_1", Toast.LENGTH_LONG).show();
                }
                else if (value.equals("1x Perbulan (Jarang Sekali)")){
                    tvValueSayur.setText("sebulan_1");
              //      Toast.makeText(LengkapiProfilActivity.this, "sebulan_1", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnInformasiDasar.setOnClickListener(this);
        btnInformasiKontak.setOnClickListener(this);
        btnInformasiPersonal.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);

        getProfile(preference.getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_informasi_dasar:

                if (linearInformasiDasar.getVisibility() == View.VISIBLE) {
                    linearInformasiDasar.setVisibility(View.GONE);
                } else {
                    linearInformasiDasar.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_informasi_kontak:

                if (linearInformasiKontak.getVisibility() == View.VISIBLE) {
                    linearInformasiKontak.setVisibility(View.GONE);
                } else {
                    linearInformasiKontak.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.btn_informasi_personal:

                if (linearInformasiPersonal.getVisibility() == View.VISIBLE) {
                    linearInformasiPersonal.setVisibility(View.GONE);
                } else {
                    linearInformasiPersonal.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.btn_simpan:
                if (edtNama.toString().isEmpty()||edtTglLahir.toString().isEmpty()||edtKota.toString().isEmpty()||edtNomor.toString().isEmpty()|| tvValueMinyak.equals("Nama Lengkap") || tvValueSayur.equals("Nama Lengkap") || tvValueMinum.equals("Nama Lengkap") || tvValueKeluhanLain.equals("Nama Lengkap") || tvValueKeluhan.equals("Nama Lengkap") || tvValueJenisKelamin.equals("Nama Lengkap") || tvValueJenisKulit.equals("Nama Lengkap")){
                    Toast.makeText(LengkapiProfilActivity.this, "Lengkap Data!!", Toast.LENGTH_LONG).show();
                }
                else {

                    String nama = edtNama.getText().toString();
                    String tgl = edtTglLahir.getText().toString();
                    String nomor = edtNomor.getText().toString();
                    String umur = tvValueUmur.getText().toString();
                    String hamil = tvValueHamil.getText().toString();
                    String jeniskulit = tvValueJenisKulit.getText().toString();
                    String kulitsensi = tvValueSensitif.getText().toString();
                    String iritasi = tvValueIritasi.getText().toString();
                    String jerawat = tvValueJerawat.getText().toString();
                    String tingkatjerawat = tvValueTingkatJerawat.getText().toString();
                    String keluhan = tvValueKeluhan.getText().toString();
                    String keluhanlain = tvValueKeluhanLain.getText().toString();
                    String minum = tvValueMinum.getText().toString();
                    String sayur = tvValueSayur.getText().toString();
                    String minyak = tvValueMinyak.getText().toString();
                    String tempat_lahir = edtKota.getText().toString();
                    String kelamin = tvValueJenisKelamin.getText().toString();


                   // updateProfile(nama,tgl,nomor,umur,hamil,jeniskulit,kulitsensi,iritasi,jerawat,tingkatjerawat,keluhan,keluhanlain,minum,sayur,minyak,tempat_lahir,kelamin);
                }
                break;

        }
    }


    private void getProfile(final String email) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.getProfile(email, preference.getToken(), Base.apiToken);
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    boolean error = response.body().getError();

                    if (!error) {

                        User data = response.body().getData();

                        edtNama.setText(data.getNama());
                        edtTempatLahir.setText(data.getTempat_lahir());
                        edtTglLahir.setText(data.getTanggal_lahir());
                        edtKota.setText(data.getKota());
                        edtAlamat.setText(data.getAlamat());
                        edtNomor.setText(data.getNomor_wa());
                        edtEmail.setText(data.getEmail());

                    } else {
                        Toast.makeText(LengkapiProfilActivity.this, "Data Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(LengkapiProfilActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LengkapiProfilActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateProfile(String nama, String tanggal_lahir, String nomor_wa, String umur, String hamil, String jenis_kulit, String kulit_sensitif, String kulit_iritasi, String berjerawat, String tingkat_jerawat, String keluhan, String keluhan_lain, String jumlah_minum, String jumlah_sayuran, String jumlah_makan_minyak, String tempat_lahir, String jenis_kelamin) {
        progressDialog.setMessage("Harap tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String email = preference.getEmail();
        String token = preference.getToken();
        String apiToken = Base.apiToken;

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> updateProfile = api.updateProfile(nama, tanggal_lahir, nomor_wa, umur, hamil, jenis_kulit, kulit_sensitif, kulit_iritasi, berjerawat, tingkat_jerawat, keluhan, keluhan_lain, jumlah_minum, jumlah_sayuran, jumlah_makan_minyak, tempat_lahir, jenis_kelamin,"kota","alamat",email,token,apiToken);
        updateProfile.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    boolean error = response.body().getError();

                    if (!error) {
                        Intent intent = new Intent(LengkapiProfilActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(LengkapiProfilActivity.this, "Update Profile Berhasil!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LengkapiProfilActivity.this, "Update Profile Gagal!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LengkapiProfilActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LengkapiProfilActivity.this, "Network Error  " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear,
                          int dayOfMonth) {
        assert dialog.getTag() != null;
        if (dialog.getTag().equals(CAL2)) {
            edtTglLahir.setText(getString(R.string.calendar, year, monthOfYear + 1, dayOfMonth));
        }
    }

}
