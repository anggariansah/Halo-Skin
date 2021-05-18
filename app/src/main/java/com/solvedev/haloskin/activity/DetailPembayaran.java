package com.solvedev.haloskin.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.RupiahConvert;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPembayaran extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private UserPreferences preference;
    private List<Produk> arrayProduk = new ArrayList<>();

    Button btnBayar;
    TableLayout tableLayoutPengiriman;
    TextView tvEkspedisi, tvOngkir, tvTotalBelanja, tvBiayaKirim, tvTotalBayar;

    String kodeProvinsi, kodeKota, kodeKecamatan, alamat, ekspedisi, ongkir, dataProduk;
    Integer totalBelanja, ongkosKirim = 0, totalBayar = 0;

    private static final int ONGKIR_REQUEST = 15;

    StringBuilder stringItem = new StringBuilder();
    RupiahConvert rupiahConvert = new RupiahConvert();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);
        btnBayar = findViewById(R.id.btn_bayar);
        tableLayoutPengiriman = findViewById(R.id.table_layout_pengiriman);
        tvEkspedisi = findViewById(R.id.tv_ekspedisi);
        tvOngkir = findViewById(R.id.tv_ongkos_kirim);
        tvTotalBelanja = findViewById(R.id.tv_total_belanja);
        tvBiayaKirim = findViewById(R.id.tv_harga_kirim);
        tvTotalBayar = findViewById(R.id.tv_total_bayar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detail Pembayaran");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            kodeKecamatan = extras.getString("kode_kecamatan");
            kodeKota = extras.getString("kode_kota");
            kodeProvinsi = extras.getString("kode_provinsi");
            alamat = extras.getString("alamat");
            totalBelanja = extras.getInt("total_belanja");

            Intent intent = getIntent();
            arrayProduk = intent.getParcelableArrayListExtra("produk");

         //   getTransaksi();

          //  Toast.makeText(this, ""+dataProduk, Toast.LENGTH_SHORT).show();
            totalBayar = totalBelanja;

            tvTotalBelanja.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalBelanja)));
            tvTotalBayar.setText(rupiahConvert.convertStringToRupiah(String.valueOf(totalBayar)));
        }

        progressDialog = new ProgressDialog(DetailPembayaran.this);
        preference = new UserPreferences(DetailPembayaran.this);

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ongkosKirim == 0){
                    Toast.makeText(DetailPembayaran.this, "Harap Pilih Jasa Pengiriman!", Toast.LENGTH_SHORT).show();
                }else{
                    totalBayar = totalBayar + ongkosKirim;

                    transaksiProduk(dataProduk);
                }
            }
        });

        tableLayoutPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(DetailPembayaran.this, OpsiPengirimanActivity.class);
                a.putExtra("kode_kecamatan", kodeKecamatan);
                startActivityForResult(a, ONGKIR_REQUEST);
            }
        });


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getTransaksi(){

        stringItem.setLength(0);

        stringItem.append("{\"items\":[");

        for (int i = 0; i < arrayProduk.size(); i++){
            stringItem.append("{\"item_id\":\"");
            stringItem.append(arrayProduk.get(i).getId_barang());
            stringItem.append("\",\"jumlah\":");
            if(i+1 == arrayProduk.size()){
                stringItem.append("\""+arrayProduk.get(i).getJumlah()+"\"}");
            }else{
                stringItem.append("\""+arrayProduk.get(i).getJumlah()+"\"},");
            }
        }

        stringItem.append("],\"ongkir\" : {\"harga\":\""+ongkir+"\", \"daerah\":\""+alamat+"\"}}");

        dataProduk = stringItem.toString();

        Log.d("DATAPRODUK",""+dataProduk);
    }

    private void deleteKeranjang(){
        for (int i = 0; i < arrayProduk.size(); i++){
            deleteCart(String.valueOf(arrayProduk.get(i).getId_barang()));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ONGKIR_REQUEST && resultCode == Activity.RESULT_OK) {
            ekspedisi = data.getStringExtra("ekspedisi");
            ongkir = data.getStringExtra("ongkir");

            int bayarOngkir = Integer.parseInt(ongkir) + totalBayar;
            ongkosKirim = Integer.parseInt(ongkir);

            tvEkspedisi.setText(ekspedisi);
            tvTotalBayar.setText(rupiahConvert.convertStringToRupiah(String.valueOf(bayarOngkir)));
            tvOngkir.setText(rupiahConvert.convertStringToRupiah(ongkir));
            tvBiayaKirim.setText(rupiahConvert.convertStringToRupiah(ongkir));

            getTransaksi();
        }
    }

    private void transaksiProduk(String dataProduk) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.transaksiProduk(preference.getEmail(), preference.getToken(), Base.apiToken, dataProduk);
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {


                        Toast.makeText(DetailPembayaran.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        deleteKeranjang();

                        Intent intent = new Intent(DetailPembayaran.this, PembayaranProdukActivity.class);
                        intent.putExtra("url", response.body().getUrl().getRedirect_url());
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(DetailPembayaran.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(DetailPembayaran.this, "error null"+response.body(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailPembayaran.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void deleteCart(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.deleteCart(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()){

                        Toast.makeText(DetailPembayaran.this, "Berhasil Dihapus dari Keranjang!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(DetailPembayaran.this, "Gagal Menghapus dari Keranjang!", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(DetailPembayaran.this, "error null :"+response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailPembayaran.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}