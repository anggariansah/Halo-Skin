package com.solvedev.haloskin.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.jbvincey.nestedradiobutton.NestedLinearRadioGroup;
import com.jbvincey.nestedradiobutton.NestedRadioButton;
import com.jbvincey.nestedradiobutton.NestedRadioGroupManager;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Pasien;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    Button btnInformasiDasar, btnInformasiKontak, btnInformasiPersonal, btnSimpan;
    private LinearLayout linearInformasiDasar, linearInformasiKontak, linearInformasiPersonal;
    private TextInputEditText edtNama, edtTempatLahir, edtTglLahir, edtKota, edtAlamat, edtNomor, edtEmail;
    ImageView ivTgl;

    private RadioButton rbLaki, rbPerempuan, rbHamil, rbTidakHamil, rbSensitif, rbTidakSensitif, rbIritasi, rbTidakIritasi;
    private RadioButton rbJerawat, rbTidakJerawat;
    private NestedRadioButton rbUmurSatu, rbUmurDua, rbUmurTiga, rbUmurEmpat, rbUmurLima;
    private NestedRadioButton rbKulitNormal, rbKulitBerminyak, rbKulitKering, rbKombinasi;
    private NestedRadioButton rbTingkatTidakAda, rbJerawatRingan, rbJerawatSedang, rbJerawatAkut;
    private NestedRadioButton rbKeluhanTidakAda, rbKeluhanBeruntusan, rbKeluhanBopeng, rbKeluhanMataPanda,
            rbKeluhanBekasJerawat, rbKeluhanLainFlek;
    private NestedRadioButton rbMinumSatu, rbMinumDua, rbMinumTiga;
    private NestedRadioButton rbSayurSatu, rbSayurDua, rbSayurTiga, rbSayurEmpat;
    private NestedRadioButton rbMinyakSatu, rbMinyakDua, rbMinyakTiga, rbMinyakEmpat, rbMinyakLima;
    private NestedRadioButton rbKusam, rbMinyakLebih, rbDehidrasi, rbPoriBesar;

    String nama = "", nomor = "", tanggal_lahir = "", tempat_lahir = "", kota = "", alamat = "", umur = "", is_hamil = "", jenis_kulit = "", is_kulit_sensitif = "", is_kulit_iritasi = "", is_kulit_berjerawat = "", tingkat_jerawat = "", keluhan = "", keluhan_lain = "", jumlah_minum = "", jumlah_sayuran = "", jumlah_makanan_minyak = "", jenis_kelamin = "";

    NestedLinearRadioGroup rgUmur, rgKulit, rgTingkatJerawat, rgKeluhanLain, rgMinum, rgSayur, rgMinyak, rgKeluhan;
    RadioGroup rgHamil, rgJk, rgSensitif, rgIritasi, rgJerawat;

    private UserPreferences preference;
    private ProgressDialog progressDialog;

    CompoundButtonGroup ckKeluhan;
    Toolbar tb;

    String CAL2 = "cal2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnInformasiDasar = findViewById(R.id.btn_informasi_dasar);
        btnInformasiKontak = findViewById(R.id.btn_informasi_kontak);
        btnInformasiPersonal = findViewById(R.id.btn_informasi_personal);
        btnSimpan = findViewById(R.id.btn_simpan);
        linearInformasiDasar = findViewById(R.id.linear_informasi_dasar);
        linearInformasiKontak = findViewById(R.id.linear_informasi_kontak);
        linearInformasiPersonal = findViewById(R.id.linear_informasi_personal);

        rbLaki = findViewById(R.id.radio_laki);
        rbPerempuan = findViewById(R.id.radio_perempuan);

        rbHamil = findViewById(R.id.radio_ya_hamil);
        rbTidakHamil = findViewById(R.id.radio_tidak_hamil);

        rbSensitif = findViewById(R.id.radio_ya_sensitif);
        rbTidakSensitif = findViewById(R.id.radio_tidak_sensitif);

        rbIritasi = findViewById(R.id.radio_ya_iritasi);
        rbTidakIritasi = findViewById(R.id.radio_tidak_iritasi);

        rbJerawat = findViewById(R.id.radio_ya_jerawat);
        rbTidakJerawat = findViewById(R.id.radio_tidak_jerawat);

        rbUmurSatu = findViewById(R.id.rg_umur_satu);
        rbUmurDua = findViewById(R.id.rg_umur_dua);
        rbUmurTiga = findViewById(R.id.rg_umur_tiga);
        rbUmurEmpat = findViewById(R.id.rg_umur_empat);
        rbUmurLima = findViewById(R.id.rg_umur_lima);

        rbKulitNormal = findViewById(R.id.radio_kulit_normal);
        rbKulitBerminyak = findViewById(R.id.radio_kulit_berminyak);
        rbKulitKering = findViewById(R.id.radio_kulit_kering);
        rbKombinasi = findViewById(R.id.radio_kulit_Kombinasi);

        rbTingkatTidakAda = findViewById(R.id.radio_tingkat_tidak_ada);
        rbJerawatRingan = findViewById(R.id.radio_jerawat_ringan);
        rbJerawatSedang = findViewById(R.id.radio_jerawat_sedang);
        rbJerawatAkut = findViewById(R.id.radio_jerawat_akut);

        rbKeluhanTidakAda = findViewById(R.id.rg_keluhan_lain_tidak_ada);
        rbKeluhanBeruntusan = findViewById(R.id.rg_keluhan_lain_beruntusan);
        rbKeluhanBopeng = findViewById(R.id.rg_keluhan_lain_bopeng);
        rbKeluhanMataPanda = findViewById(R.id.rg_keluhan_lain_mata_panda);
        rbKeluhanBekasJerawat = findViewById(R.id.rg_keluhan_bekas_jerawat);
        rbKeluhanLainFlek = findViewById(R.id.rg_keluhan_lain_flek);

        rbMinumSatu = findViewById(R.id.radio_minum_satu);
        rbMinumDua = findViewById(R.id.radio_minum_dua);
        rbMinumTiga = findViewById(R.id.radio_minum_tiga);

        rbSayurSatu = findViewById(R.id.radio_sayur_satu);
        rbSayurDua = findViewById(R.id.radio_sayur_dua);
        rbSayurTiga = findViewById(R.id.radio_sayur_tiga);
        rbSayurEmpat = findViewById(R.id.radio_sayur_empat);

        rbMinyakSatu = findViewById(R.id.radio_minyak_satu);
        rbMinyakDua = findViewById(R.id.radio_minyak_dua);
        rbMinyakTiga = findViewById(R.id.radio_minyak_tiga);
        rbMinyakEmpat = findViewById(R.id.radio_minyak_empat);
        rbMinyakLima = findViewById(R.id.radio_minyak_lima);

        rbKusam = findViewById(R.id.rb_keluhan_kusam);
        rbMinyakLebih = findViewById(R.id.rb_keluhan_minyak);
        rbDehidrasi = findViewById(R.id.rb_keluhan_dehidrasi);
        rbPoriBesar = findViewById(R.id.rb_keluhan_pori_besar);

        edtNama = findViewById(R.id.edt_nama);
        edtTempatLahir = findViewById(R.id.edt_tempat_lahir);
        edtTglLahir = findViewById(R.id.edt_tanggal_lahir);
        edtKota = findViewById(R.id.edt_kota);
        edtAlamat = findViewById(R.id.edt_alamat_rumah);
        edtNomor = findViewById(R.id.edt_nomor);
        edtEmail = findViewById(R.id.edt_email);
        btnSimpan = findViewById(R.id.btn_simpan);
        ivTgl = findViewById(R.id.iv_tanggal);

        tb = findViewById(R.id.tb_lengkapi);

        progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        preference = new UserPreferences(UpdateProfileActivity.this);

        //find id radio
        rgUmur = findViewById(R.id.rg_umur);
        rgHamil = findViewById(R.id.rg_hamil);
        rgJk = findViewById(R.id.rg_jenis_kelamin);
        rgKulit = findViewById(R.id.rg_kulit);
        rgSensitif = findViewById(R.id.rg_sensitif);
        rgIritasi = findViewById(R.id.rg_iritasi);
        rgJerawat = findViewById(R.id.rg_jerawat);
        rgTingkatJerawat = findViewById(R.id.rg_tingkat_jerawat);
        rgKeluhanLain = findViewById(R.id.rg_keluhan_lain);
        ckKeluhan = findViewById(R.id.ck_keluhan);
        rgMinum = findViewById(R.id.rg_minum);
        rgSayur = findViewById(R.id.rg_sayur);
        rgMinyak = findViewById(R.id.rg_minyak);
        rgKeluhan = findViewById(R.id.rg_keluhan);

        ivTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp1 = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(UpdateProfileActivity.this);
                cdp1.show(getSupportFragmentManager(), CAL2);
            }
        });


        rgKeluhan.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_keluhan_kusam:
                        keluhan = "kusam";
                        break;
                    case R.id.rb_keluhan_minyak:
                        keluhan = "minyak_berlebih";
                        break;
                    case R.id.rb_keluhan_dehidrasi:
                        keluhan = "dehidrasi";
                        break;
                    case R.id.rb_keluhan_pori_besar:
                        keluhan = "pori_besar";
                        break;

                }
            }
        });

        //radio button umur
        rgUmur.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_umur_satu:
                        umur = "<18";
                        break;
                    case R.id.rg_umur_dua:
                        umur = "18-24";
                        break;
                    case R.id.rg_umur_tiga:
                        umur = "25-34";
                        break;
                    case R.id.rg_umur_empat:
                        umur = "35-44";
                        break;
                    case R.id.rg_umur_lima:
                        umur = ">45";
                        break;

                }
            }
        });

        //radio button jenis kelamin
        rgJk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_laki:
                        jenis_kelamin = "male";
                        break;

                    case R.id.radio_perempuan:
                        jenis_kelamin = "female";
                        break;
                }
            }
        });

        //radio hamil
        rgHamil.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_hamil:
                        is_hamil = "Y";
                        break;

                    case R.id.radio_tidak_hamil:
                        is_hamil = "N";
                        break;
                }
            }
        });

        // radio kulit
        rgKulit.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_kulit_normal:
                        jenis_kulit = "normal";
                        break;

                    case R.id.radio_kulit_berminyak:
                        jenis_kulit = "berminyak";
                        break;

                    case R.id.radio_kulit_kering:
                        jenis_kulit = "kering";
                        break;

                    case R.id.radio_kulit_Kombinasi:
                        jenis_kulit = "kombinasi";
                        break;
                }
            }
        });

        rgSensitif.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_sensitif:
                        is_kulit_sensitif = "Y";
                        break;
                    case R.id.radio_tidak_sensitif:
                        is_kulit_sensitif = "N";
                        break;
                }
            }
        });

        rgIritasi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_iritasi:
                        is_kulit_iritasi = "Y";
                        break;
                    case R.id.radio_tidak_iritasi:
                        is_kulit_iritasi = "N";
                        break;
                }
            }
        });

        rgJerawat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ya_jerawat:
                        is_kulit_berjerawat = "Y";
                        break;
                    case R.id.radio_tidak_jerawat:
                        is_kulit_berjerawat = "N";
                        break;
                }

            }
        });

        // radio tingkat jerawat
        rgTingkatJerawat.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_tingkat_tidak_ada:
                        tingkat_jerawat = "tidak_ada";
                        break;
                    case R.id.radio_jerawat_ringan:
                        tingkat_jerawat = "ringan";
                        break;
                    case R.id.radio_jerawat_sedang:
                        tingkat_jerawat = "sedang";
                        break;
                    case R.id.radio_jerawat_akut:
                        tingkat_jerawat = "meradang";
                        break;

                }
            }
        });

        // radio keluhan lain
        rgKeluhanLain.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_keluhan_lain_tidak_ada:
                        keluhan_lain = "tidak_ada";
                        break;
                    case R.id.rg_keluhan_lain_beruntusan:
                        keluhan_lain = "bruntusan";
                        break;
                    case R.id.rg_keluhan_lain_bopeng:
                        keluhan_lain = "scar";
                        break;
                    case R.id.rg_keluhan_lain_mata_panda:
                        keluhan_lain = "mata_panda";
                        break;
                    case R.id.rg_keluhan_bekas_jerawat:
                        keluhan_lain = "bekas_jerawat";
                        break;
                    case R.id.rg_keluhan_lain_flek:
                        keluhan_lain = "flek";
                        break;
                }
            }
        });


        //radio minum
        rgMinum.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_minum_satu:
                        jumlah_minum = "1-3";
                        break;
                    case R.id.radio_minum_dua:
                        jumlah_minum = "4-7";
                        break;
                    case R.id.radio_minum_tiga:
                        jumlah_minum = ">=8";
                        break;
                }
            }
        });

        //radio sayur
        rgSayur.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_sayur_satu:
                        jumlah_sayuran = "setiap_hari";
                        break;
                    case R.id.radio_sayur_dua:
                        jumlah_sayuran = "seminggu_1";
                        break;
                    case R.id.radio_sayur_tiga:
                        jumlah_sayuran = "seminggu_2_3";
                        break;
                    case R.id.radio_sayur_empat:
                        jumlah_sayuran = "seminggu_4_5";
                        break;
                }
            }
        });


        //radio minyak
        rgMinyak.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_minyak_satu:
                        jumlah_makanan_minyak = "setiap_hari";
                        break;
                    case R.id.radio_minyak_dua:
                        jumlah_makanan_minyak = "seminggu_1";
                        break;
                    case R.id.radio_minyak_tiga:
                        jumlah_makanan_minyak = "seminggu_2_4";
                        break;
                    case R.id.radio_minyak_empat:
                        jumlah_makanan_minyak = "duaminggu_1";
                        break;
                    case R.id.radio_minyak_lima:
                        jumlah_makanan_minyak = "sebulan_1";
                        break;
                }
            }
        });

        btnInformasiDasar.setOnClickListener(this);
        btnInformasiKontak.setOnClickListener(this);
        btnInformasiPersonal.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);

        getProfile();

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
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

                nama = edtNama.getText().toString();
                tempat_lahir = edtTempatLahir.getText().toString();
                tanggal_lahir = edtTglLahir.getText().toString();
                kota = edtKota.getText().toString();
                alamat = edtAlamat.getText().toString();
                nomor = edtNomor.getText().toString();

                if (nama.equals("") || tanggal_lahir.equals("") || nomor.equals("") || umur.equals("") || is_hamil.equals("") ||
                        is_kulit_sensitif.equals("") || is_kulit_iritasi.equals("") || is_kulit_berjerawat.equals("") ||
                        tingkat_jerawat.equals("") || keluhan.equals("") || keluhan_lain.equals("") || jumlah_minum.equals("") || jumlah_sayuran.equals("") ||
                        jumlah_makanan_minyak.equals("") || tempat_lahir.equals("") || jenis_kelamin.equals("")) {

                    Toast.makeText(this, "Harap Lengkapi isian Anda!!", Toast.LENGTH_SHORT).show();
                } else {
                    update(nama, tanggal_lahir, nomor, umur, is_hamil, jenis_kulit, is_kulit_sensitif, is_kulit_iritasi, is_kulit_berjerawat, tingkat_jerawat, keluhan, keluhan_lain, jumlah_minum, jumlah_sayuran, jumlah_makanan_minyak, tempat_lahir, jenis_kelamin, kota, alamat);
                }

                break;

        }
    }

    public void update(String nama, String tanggal_lahir, String nomor, String umur, String is_hamil, String jenis_kulit, String is_kulit_sensitif, String is_kulit_iritasi, String is_kulit_berjerawat, String tingkat_jerawat, String keluhan, String keluhan_lain, String jumlah_minum, String jumlah_sayuran, String jumlah_makanan_minyak, String tempat_lahir, String jenis_kelamin, String kota, String alamat) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> update = api.updateProfile(nama, tanggal_lahir, nomor, umur, is_hamil, jenis_kulit, is_kulit_sensitif, is_kulit_iritasi, is_kulit_berjerawat, tingkat_jerawat, keluhan, keluhan_lain, jumlah_minum, jumlah_sayuran, jumlah_makanan_minyak, tempat_lahir, jenis_kelamin, kota, alamat, preference.getEmail(), preference.getToken(), Base.apiToken);
        update.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                if (response.body() != null) {
                    boolean error = response.body().getError();

                    if (!error) {
                        Toast.makeText(UpdateProfileActivity.this, "Update Data Berhasil!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateProfileActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Gagal Update : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "NULL OBJECT", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Erorr!!" + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getProfile() {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.getProfile(preference.getEmail(), preference.getToken(), Base.apiToken);
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

                        jenis_kelamin = "female";

                        if (data.getJenis_kelamin() != null) {
                            switch (data.getJenis_kelamin()) {
                                case "male":
                                    rbLaki.setChecked(true);
                                    break;
                                case "female":
                                    rbPerempuan.setChecked(true);
                            }
                        }

                        Pasien dataPasien = response.body().getData_pasien();

                        if (dataPasien != null) {

                            if (dataPasien.getUmur() != null) {
                                switch (dataPasien.getUmur()) {
                                    case "<18":
                                        rbUmurSatu.setChecked(true);
                                        break;
                                    case "18-24":
                                        rbUmurDua.setChecked(true);
                                        break;
                                    case "25-34":
                                        rbUmurTiga.setChecked(true);
                                        break;
                                    case "35-44":
                                        rbUmurEmpat.setChecked(true);
                                        break;
                                    case ">45":
                                        rbUmurLima.setChecked(true);
                                        break;

                                }
                            }

                            if (dataPasien.getIs_hamil() != null) {
                                switch (dataPasien.getIs_hamil()) {
                                    case "Y":
                                        rbHamil.setChecked(true);
                                        break;
                                    case "N":
                                        rbTidakHamil.setChecked(true);
                                        break;
                                }
                            }


                            if (dataPasien.getJenis_kulit() != null) {
                                switch (dataPasien.getJenis_kulit()) {
                                    case "normal":
                                        rbKulitNormal.setChecked(true);
                                        break;
                                    case "berminyak":
                                        rbKulitBerminyak.setChecked(true);
                                        break;
                                    case "kering":
                                        rbKulitKering.setChecked(true);
                                        break;
                                    case "kombinasi":
                                        rbKombinasi.setChecked(true);
                                        break;

                                }
                            }

                            if (dataPasien.getIs_kulit_sensitif() != null) {
                                switch (dataPasien.getIs_kulit_sensitif()) {
                                    case "Y":
                                        rbSensitif.setChecked(true);
                                        break;
                                    case "N":
                                        rbTidakSensitif.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getIs_kulit_iritasi() != null) {
                                switch (dataPasien.getIs_kulit_iritasi()) {
                                    case "Y":
                                        rbIritasi.setChecked(true);
                                        break;
                                    case "N":
                                        rbTidakIritasi.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getIs_kulit_berjerawat() != null) {
                                switch (dataPasien.getIs_kulit_berjerawat()) {
                                    case "Y":
                                        rbJerawat.setChecked(true);
                                        break;
                                    case "N":
                                        rbTidakJerawat.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getTingkat_jerawat() != null) {
                                switch (dataPasien.getTingkat_jerawat()) {
                                    case "tidak_ada":
                                        rbTingkatTidakAda.setChecked(true);
                                        break;
                                    case "ringan":
                                        rbJerawatRingan.setChecked(true);
                                        break;
                                    case "sedang":
                                        rbJerawatSedang.setChecked(true);
                                        break;
                                    case "meradang":
                                        rbJerawatAkut.setChecked(true);
                                        break;

                                }
                            }

                            if (dataPasien.getKeluhan_lain() != null) {
                                switch (dataPasien.getKeluhan_lain()) {
                                    case "tidak_ada":
                                        rbKeluhanTidakAda.setChecked(true);
                                        break;
                                    case "bruntusan":
                                        rbKeluhanBeruntusan.setChecked(true);
                                        break;
                                    case "scar":
                                        rbKeluhanBopeng.setChecked(true);
                                        break;
                                    case "mata_panda":
                                        rbKeluhanMataPanda.setChecked(true);
                                        break;
                                    case "bekas_jerawat":
                                        rbKeluhanBekasJerawat.setChecked(true);
                                        break;
                                    case "flek":
                                        rbKeluhanLainFlek.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getJumlah_minum() != null) {
                                switch (dataPasien.getJumlah_minum()) {
                                    case "1-3":
                                        rbMinumSatu.setChecked(true);
                                        break;
                                    case "4-7":
                                        rbMinumDua.setChecked(true);
                                        break;
                                    case ">=8":
                                        rbMinumTiga.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getJumlah_sayuran() != null) {
                                switch (dataPasien.getJumlah_sayuran()) {
                                    case "setiap_hari":
                                        rbSayurSatu.setChecked(true);
                                        break;
                                    case "seminggu_1":
                                        rbSayurDua.setChecked(true);
                                        break;
                                    case "seminggu_2_3":
                                        rbSayurTiga.setChecked(true);
                                        break;
                                    case "seminggu_4_5":
                                        rbSayurEmpat.setChecked(true);
                                        break;
                                }
                            }


                            if (dataPasien.getJumlah_makanan_minyak() != null) {
                                switch (dataPasien.getJumlah_makanan_minyak()) {
                                    case "setiap_hari":
                                        rbMinyakSatu.setChecked(true);
                                        break;
                                    case "seminggu_1":
                                        rbMinyakDua.setChecked(true);
                                        break;
                                    case "seminggu_2_4":
                                        rbMinyakTiga.setChecked(true);
                                        break;
                                    case "duaminggu_1":
                                        rbMinyakEmpat.setChecked(true);
                                        break;
                                    case "sebulan_1":
                                        rbMinyakLima.setChecked(true);
                                        break;
                                }
                            }

                            if (dataPasien.getKeluhan() != null) {
                                switch (dataPasien.getKeluhan()) {
                                    case "kusam":
                                        rbKusam.setChecked(true);
                                        break;
                                    case "minyak_berlebih":
                                        rbMinyakLebih.setChecked(true);
                                        break;
                                    case "dehidrasi":
                                        rbDehidrasi.setChecked(true);
                                        break;
                                    case "pori_besar":
                                        rbPoriBesar.setChecked(true);
                                        break;

                                }
                            }

                        }

                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Data Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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