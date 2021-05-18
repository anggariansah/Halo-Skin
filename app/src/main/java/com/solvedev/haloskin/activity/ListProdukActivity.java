package com.solvedev.haloskin.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.ItemAdapter;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.model.Resep;
import com.solvedev.haloskin.model.ResepResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.ImageUtils;
import com.solvedev.haloskin.utils.RupiahConvert;
import com.solvedev.haloskin.utils.UserPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProdukActivity extends AppCompatActivity {

    private List<Produk> listProduk = new ArrayList<>();
    private List<Produk> listSearchProduk = new ArrayList<>();
    private RecyclerView mRvProduk;
    private TextView tvNama, tvHarga, tvTotalHarga, tvTotalProduk, tvDeskripsi;
    private SearchView svTitle;
    private ImageView ivAddFavorite, ivRemoveFavorite, ivFoto;

    private ItemAdapter adapterProduk;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private Integer idType, idJenis, harga, totalHarga = 0, idBarang, jumlah = 0, statusButton = 0;
    private String nama, foto, deskripsi, fotoResep;

    private Button btnKeranjang, btnChat, btnUpload, btnAddDialog, btnDeleteDialog, btnCekResep;
    private static final int GALLERY_REQUEST = 1;

    private LinearLayout bottomKeranjang;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        mRvProduk = findViewById(R.id.rv_produk);
        bottomKeranjang = findViewById(R.id.bottom_sheet);
        tvTotalHarga = findViewById(R.id.tv_total_harga);
        tvTotalProduk = findViewById(R.id.tv_total_produk);
        btnKeranjang = findViewById(R.id.btn_keranjang);
        svTitle = findViewById(R.id.sv_title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Beli Produk");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idType = extras.getInt("id_type");
            idJenis = extras.getInt("id_jenis");
        }

        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);

        if (idType == 3) {
            getListProdukResep();
        } else {
            getListProduk();
        }

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListProdukActivity.this, KeranjangActivity.class);
                startActivity(intent);
                finish();

            }
        });

        svTitle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    getListProduk();
                } else {
                    getSearchProduk(newText);
                }

//                int textlength = newText.length();
//
//                listProduk.clear();
//
//                for (int i = 0; i < listSearchProduk.size(); i++) {
//                    if (textlength <= listSearchProduk.get(i).getNama().length()) {
//                        if (newText.equalsIgnoreCase((String) listSearchProduk.get(i).getNama().subSequence(0, textlength))) {
//                            Produk data = new Produk();
//                            data.setId(listSearchProduk.get(i).getId());
//                            data.setNama(listSearchProduk.get(i).getNama());
//                            data.setFoto(listSearchProduk.get(i).getFoto());
//                            data.setHarga(listSearchProduk.get(i).getHarga());
//
//                            listProduk.add(data);
//                        }
//                    }
//                }
//
//
//                setAdapter();

                return false;
            }
        });


    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getListProduk() {

        listProduk.clear();

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> getData = api.getListProduk(preference.getEmail(), preference.getToken(), Base.apiToken, idType, idJenis);
        getData.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {

                if (response.body() != null) {

                    listProduk = response.body().getData();

                    if (listProduk != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(ListProdukActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getListProdukResep() {

        listProduk.clear();

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> getData = api.getListProdukResep(preference.getEmail(), preference.getToken(), Base.apiToken, idType, idJenis);
        getData.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {

                if (response.body() != null) {

                    listProduk = response.body().getData();

                    if (listProduk != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(ListProdukActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getSearchProduk(String nama) {

        listProduk.clear();

        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> getData = api.getSearchProduk(preference.getEmail(), preference.getToken(), Base.apiToken, idType, idJenis, nama);
        getData.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {

                if (response.body() != null) {

                    listProduk = response.body().getData();

                    if (listProduk != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(ListProdukActivity.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAdapter() {
        mRvProduk.setLayoutManager(new GridLayoutManager(ListProdukActivity.this, 2));
        mRvProduk.setHasFixedSize(true);
        adapterProduk = new ItemAdapter(ListProdukActivity.this, listProduk, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                Produk model = listProduk.get(posisition);

                nama = model.getNama();
                harga = model.getHarga();
                idBarang = model.getId();
                foto = model.getFoto();
                deskripsi = model.getDeskripsi();

                showDetail(String.valueOf(idBarang));

            }
        }, new ItemAdapter.OnButtonTambahClickListener() {
            @Override
            public void onButtonTambahClick(View v, int posisition) {

                Produk model = listProduk.get(posisition);


                jumlah = jumlah + 1;

                idBarang = model.getId();


                if (jumlah >= 1) {
                    bottomKeranjang.setVisibility(View.VISIBLE);
                }

                tvTotalProduk.setText(jumlah + " Produk");

                totalHarga = totalHarga + model.getHarga();

                tvTotalHarga.setText(String.valueOf(totalHarga));

                if (idType == 3) {
                    showAlertResep();
                } else {
                    addCart("button", String.valueOf(model.getId()));
                }


            }
        }, new ItemAdapter.OnButtonDeleteClickListener() {
            @Override
            public void onButtonDeleteClick(View v, int posisition) {

                Produk model = listProduk.get(posisition);

                jumlah = jumlah - 1;

                if (jumlah == 0) {
                    bottomKeranjang.setVisibility(View.GONE);
                }

                tvTotalProduk.setText(jumlah + " Produk");

                totalHarga = totalHarga - model.getHarga();

                tvTotalHarga.setText(String.valueOf(totalHarga));

                deleteCart(String.valueOf(model.getId()));


            }
        });
        mRvProduk.setAdapter(adapterProduk);
        adapterProduk.notifyDataSetChanged();

    }


    public void showDetail(final String id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_detail_produk, null);

        tvNama = v.findViewById(R.id.tv_nama);
        tvHarga = v.findViewById(R.id.tv_harga);
        ivAddFavorite = v.findViewById(R.id.iv_add_favorite);
        tvDeskripsi = v.findViewById(R.id.tv_desk);
        ivFoto = v.findViewById(R.id.iv_foto);
        ivRemoveFavorite = v.findViewById(R.id.iv_remove_favorite);
        btnAddDialog = v.findViewById(R.id.btn_tambah);
        btnDeleteDialog = v.findViewById(R.id.btn_hapus);

        final AlertDialog dialog = builder.create();

        tvNama.setText(nama);
        tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(harga)));
        tvDeskripsi.setText(deskripsi);

        ivAddFavorite.setVisibility(View.GONE);
        ivRemoveFavorite.setVisibility(View.VISIBLE);

        Glide
                .with(this)
                .load(Base.baseImageUrl + foto)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(ivFoto);


        cekFavorite();

        if (statusButton == 0) {
            btnDeleteDialog.setVisibility(View.GONE);
            btnAddDialog.setVisibility(View.VISIBLE);
        } else {
            btnDeleteDialog.setVisibility(View.VISIBLE);
            btnAddDialog.setVisibility(View.GONE);
        }

        ivAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavorite(String.valueOf(idBarang));
            }
        });

        ivRemoveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite(String.valueOf(idBarang));
            }
        });

        btnAddDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idType == 3) {
                    showAlertResep();
                } else {
                    addCart("dialog", id);
                }
            }
        });

        btnDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCart(id);
            }
        });

        dialog.setView(v);

        dialog.show();
    }


    public void showAlertResep() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_beli_produk, null);

        btnChat = v.findViewById(R.id.btn_chat);
        btnUpload = v.findViewById(R.id.btn_upload_resep);
        btnCekResep = v.findViewById(R.id.btn_cek_resep);

        final AlertDialog dialog = builder.create();

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProdukActivity.this, ListDokterActivity.class);
                startActivity(intent);
            }
        });

        btnCekResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekResep();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ListProdukActivity.this)
                        .setItems(R.array.camera, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String text = getResources().getStringArray(R.array.camera)[which];
                                if (text.equals("Camera")) {
                                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(i, 0);
                                } else {
                                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                                }

                            }
                        }).show();

            }
        });

        dialog.setView(v);

        dialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);

                fotoResep = ImageUtils.bitmapToBase64String(bitmap, 50);

                addResep(String.valueOf(idBarang), fotoResep);

            } catch (IOException e) {

                e.printStackTrace();
            }
        } else if (requestCode == 0) {

            bitmap = (Bitmap) data.getExtras().get("data");

            fotoResep = ImageUtils.bitmapToBase64String(bitmap, 50);

            addResep(String.valueOf(idBarang), fotoResep);

        }
    }

    private void cekFavorite() {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> login = api.getFavorite(preference.getEmail(), preference.getToken(), Base.apiToken);
        login.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {
                        List<Produk> produks = response.body().getData();

                        for (int i = 0; i < produks.size(); i++) {

                            Integer idBarangList = produks.get(i).getId_barang();

                            if (idBarangList == idBarang) {
                                ivAddFavorite.setVisibility(View.VISIBLE);
                                ivRemoveFavorite.setVisibility(View.GONE);
                            }
                        }
                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void cekResep() {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ResepResponse> data = api.getResep(preference.getEmail(), preference.getToken(), Base.apiToken);
        data.enqueue(new Callback<ResepResponse>() {
            @Override
            public void onResponse(Call<ResepResponse> call, Response<ResepResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {
                        List<Resep> produks = response.body().getData();

                        if (produks.size() != 0) {
                            for (int i = 0; i < produks.size(); i++) {

                                if (produks.get(i).getId() == idBarang) {

                                    addCart("resep", String.valueOf(idBarang));

                                } else {
                                    Toast.makeText(ListProdukActivity.this, "Resep Belum Dikonfirmasi !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            Toast.makeText(ListProdukActivity.this, "Resep Belum Dikonfirmasi!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ListProdukActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResepResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addFavorite(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.addFavorite(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        ivAddFavorite.setVisibility(View.VISIBLE);
                        ivRemoveFavorite.setVisibility(View.GONE);

                        Toast.makeText(ListProdukActivity.this, "Favorite Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();

                    } else {
                        //  Toast.makeText(ListProdukActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addResep(String idBarang, String gambar) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ResepResponse> data = api.addResep(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang, gambar);
        data.enqueue(new Callback<ResepResponse>() {
            @Override
            public void onResponse(Call<ResepResponse> call, Response<ResepResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Toast.makeText(ListProdukActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ListProdukActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResepResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addCart(final String jenis, String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.addCart(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Toast.makeText(ListProdukActivity.this, "Berhasil Ditambahkan Keranjang!", Toast.LENGTH_SHORT).show();
                        statusButton = 1;

                        if (jenis.equals("dialog")) {
                            if (statusButton == 0) {
                                btnDeleteDialog.setVisibility(View.GONE);
                                btnAddDialog.setVisibility(View.VISIBLE);
                            } else {
                                btnDeleteDialog.setVisibility(View.VISIBLE);
                                btnAddDialog.setVisibility(View.GONE);
                            }
                        } else if (jenis.equals("resep")) {
                            Intent intent = new Intent(ListProdukActivity.this, KeranjangActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // Toast.makeText(ListProdukActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Toast.makeText(ListProdukActivity.this, "Berhasil Dihapus dari Keranjang!", Toast.LENGTH_SHORT).show();
                        statusButton = 0;

                        if (statusButton == 0) {
                            btnDeleteDialog.setVisibility(View.GONE);
                            btnAddDialog.setVisibility(View.VISIBLE);
                        } else {
                            btnDeleteDialog.setVisibility(View.VISIBLE);
                            btnAddDialog.setVisibility(View.GONE);
                        }

                    } else {
                        // Toast.makeText(ListProdukActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteFavorite(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.deleteFavorite(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        ivAddFavorite.setVisibility(View.GONE);
                        ivRemoveFavorite.setVisibility(View.VISIBLE);

                        Toast.makeText(ListProdukActivity.this, "Favorite Berhasil Dihapus!", Toast.LENGTH_SHORT).show();

                    } else {
                        //  Toast.makeText(ListProdukActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ListProdukActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListProdukActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
