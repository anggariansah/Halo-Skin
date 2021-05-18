package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.DetailTransaksiAdapter;
import com.solvedev.haloskin.adapter.RiwayatTransaksiAdapter;
import com.solvedev.haloskin.model.ItemTransaksi;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.model.Transaksi;
import com.solvedev.haloskin.model.TransaksiResponse;
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

public class RiwayatPembelianActivity extends AppCompatActivity {

    private Toolbar tb;
    private RecyclerView mRvProdukDikirim, mRvDetail;
    private ProgressDialog progressDialog;
    private RiwayatTransaksiAdapter adapterRiwayat;
    private DetailTransaksiAdapter adapterDetail;
    private List<Transaksi> listTransaksi = new ArrayList<>();
    private List<Transaksi> listDetail = new ArrayList<>();
    private UserPreferences preferences;
    private TextView tvNama,tvInvoice,tvPengiriman, tvPembayaranMelalui, tvDiskon,tvJumlah,tvTotalBayar,tvTotalBelanja,tvOngkir,tvTanggal,tvHarga;
    String nama, jumlah, pengiriman, pembayaran, tanggal, invoice;
    Integer totalBelanja, bayar, diskon, harga;

    private RupiahConvert rupiahConvert = new RupiahConvert();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembelian);

        tb = findViewById(R.id.tb_riwayat_pembelian);
        mRvProdukDikirim = findViewById(R.id.rv_produk_dikirim);

        progressDialog = new ProgressDialog(this);
        preferences = new UserPreferences(this);


        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        getRiwayatBeli();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getRiwayatBeli() {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<TransaksiResponse> getData = api.getRiwayatBeli(preferences.getEmail(), preferences.getToken(), Base.apiToken);
        getData.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {

                if(response.body() != null){

                    List<Transaksi> listTrans = response.body().getData();

                    for (Transaksi modelTransaksi : listTrans){

                        List<ItemTransaksi> listItem = modelTransaksi.getList_item();

                        for (ItemTransaksi modelItem : listItem){

                            Transaksi model = new Transaksi();
                            model.setId(modelItem.getId());
                            model.setIdtransaksi(modelTransaksi.getId());
                            model.setNo_invoice(modelTransaksi.getNo_invoice());
                            model.setId_user(modelTransaksi.getId_user());
                            model.setJumlah(modelTransaksi.getJumlah());
                            model.setMethod(modelTransaksi.getMethod());
                            model.setStatus(modelTransaksi.getStatus());
                            model.setNo_resi(modelTransaksi.getNo_resi());
                            model.setName(modelItem.getName());
                            model.setPrice(modelItem.getPrice());
                            model.setQuantity(modelItem.getQuantity());
                            model.setTgl_transaksi(modelTransaksi.getTgl_transaksi());

                            Log.d("", ""+listItem.size());

                            if (!model.getName().equals("Ongkos Kirim")){
                                listTransaksi.add(model);
                            }


                        }


                    }

                    if (listTransaksi !=null){

                        setAdapter();
                    }
                    else {
                        Toast.makeText(RiwayatPembelianActivity.this, "Data Tidak Ada!!", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(RiwayatPembelianActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RiwayatPembelianActivity.this, "Network Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailRiwayat(Integer idtransaksi) {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        listDetail.clear();

        Log.d("DETAIL", "IDTRANS="+idtransaksi);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<TransaksiResponse> getData = api.getDetailRiwayat(preferences.getEmail(), preferences.getToken(), Base.apiToken, String.valueOf(idtransaksi));
        getData.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {

                if(response.body() != null){

                    List<Transaksi> listTrans = response.body().getData();

                    for (Transaksi modelTransaksi : listTrans){

                        List<ItemTransaksi> listItem = modelTransaksi.getList_item();

                        for (ItemTransaksi modelItem : listItem){

                            Transaksi model = new Transaksi();
                            model.setId(modelItem.getId());
                            model.setNo_invoice(modelTransaksi.getNo_invoice());
                            model.setId_user(modelTransaksi.getId_user());
                            model.setJumlah(modelTransaksi.getJumlah());
                            model.setMethod(modelTransaksi.getMethod());
                            model.setStatus(modelTransaksi.getStatus());
                            model.setNo_resi(modelTransaksi.getNo_resi());
                            model.setName(modelItem.getName());
                            model.setPrice(modelItem.getPrice());
                            model.setQuantity(modelItem.getQuantity());
                            model.setTgl_transaksi(modelTransaksi.getTgl_transaksi());

                            listDetail.add(model);

                            Log.d("DETAIL", ""+listDetail.size());

                        }


                    }

                    if (listDetail !=null){

                        setAdapterDetail();
                    }
                    else {
                        Toast.makeText(RiwayatPembelianActivity.this, "Data Tidak Ada!!", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(RiwayatPembelianActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RiwayatPembelianActivity.this, "Network Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCart(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.addCart(preferences.getEmail(), preferences.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()){

                        Toast.makeText(RiwayatPembelianActivity.this, "Berhasil Ditambahkan Keranjang!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RiwayatPembelianActivity.this, KeranjangActivity.class);
                        startActivity(intent);

                    }else{
                        Intent intent = new Intent(RiwayatPembelianActivity.this, KeranjangActivity.class);
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(RiwayatPembelianActivity.this, "error null :"+response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RiwayatPembelianActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    public  void showDetail(Integer id){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_detail_riwayat, null);

        tvPengiriman = v.findViewById(R.id.tv_pengiriman);
        tvTanggal = v.findViewById(R.id.tv_tanggal);
        tvDiskon = v.findViewById(R.id.tv_jumlah_diskon);
        tvTotalBelanja = v.findViewById(R.id.tv_total_belanja);
        tvTotalBayar = v.findViewById(R.id.tv_total_bayar);
        tvInvoice = v.findViewById(R.id.tv_invoice);
        mRvDetail = v.findViewById(R.id.rv_detail);

        final AlertDialog dialog = builder.create();


        tvPengiriman.setText(pengiriman != null ? pengiriman : "");
        tvTotalBelanja.setText(totalBelanja != null ? rupiahConvert.convertStringToRupiah(String.valueOf(totalBelanja)) : "");
        tvTotalBayar.setText(bayar != null ? rupiahConvert.convertStringToRupiah(String.valueOf(bayar)) : "");
        tvDiskon.setText(diskon != null ? String.valueOf(diskon) : "");
        tvInvoice.setText(invoice != null ? invoice : "");


//        if(totalBelanja != null && harga != null && jumlah != null){
//            int ongkir = totalBelanja - (harga * Integer.parseInt(jumlah));
//            tvOngkir.setText(rupiahConvert.convertStringToRupiah(String.valueOf(ongkir)));
//        }else{
//            tvOngkir.setText("");
//        }


        getDetailRiwayat(id);



        dialog.setView(v);

        dialog.show();


    }
    public void setAdapter() {
        mRvProdukDikirim.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvProdukDikirim.setHasFixedSize(true);
        adapterRiwayat = new RiwayatTransaksiAdapter(RiwayatPembelianActivity.this, listTransaksi, new RiwayatTransaksiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                Transaksi model = listTransaksi.get(posisition);

                nama = model.getName();
                jumlah = model.getQuantity();
                harga = model.getPrice();
                pengiriman = model.getMethod();
                totalBelanja = model.getJumlah();
                diskon = model.getDiskon();
                bayar = model.getJumlah();
                invoice = model.getNo_invoice();

                Log.d("DETAIL","ID="+model.getIdtransaksi());


                showDetail(model.getIdtransaksi());

            }
        }, new RiwayatTransaksiAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View v, int posisition) {


                Transaksi model = listTransaksi.get(posisition);

                addCart(String.valueOf(model.getId()));


            }
        });
        mRvProdukDikirim.setAdapter(adapterRiwayat);
        adapterRiwayat.notifyDataSetChanged();
    }

    public void setAdapterDetail() {
        mRvDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvDetail.setHasFixedSize(true);
        adapterDetail = new DetailTransaksiAdapter(RiwayatPembelianActivity.this, listDetail, new DetailTransaksiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

            }
        });
        mRvDetail.setAdapter(adapterDetail);
        adapterDetail.notifyDataSetChanged();
    }


}