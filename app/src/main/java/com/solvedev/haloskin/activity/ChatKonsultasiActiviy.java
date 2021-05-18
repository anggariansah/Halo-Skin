package com.solvedev.haloskin.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jsibbold.zoomage.ZoomageView;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.ChatAdapter;
import com.solvedev.haloskin.model.Chat;
import com.solvedev.haloskin.model.ChatProduk;
import com.solvedev.haloskin.model.ChatResponse;
import com.solvedev.haloskin.model.DataModel;
import com.solvedev.haloskin.model.Notification;
import com.solvedev.haloskin.model.NotificationResponse;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.model.StartChatResponse;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.service.Common;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.ImageUtils;
import com.solvedev.haloskin.utils.UserPreferences;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatKonsultasiActiviy extends AppCompatActivity {

    private List<Chat> listChat = new ArrayList<>();
    private RecyclerView mRvChat;
    private EditText edtPesan;
    private ImageButton ibSend;
    private TextView tvDetik, tvNama;

    String foto, date, nama, nama_profile, room_id, chatKonsul, jenis = "chat", notif_token, tgl_transaksi, status_chat;

    private ChatAdapter adapterChat;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private LinearLayout linearAttachCamera, linearAttachGallery, linearSend, linearTimer;
    private Toolbar tb;
    private SwipeRefreshLayout swipeRefreshLayout;

    StringBuilder stringItem = new StringBuilder();

    public static final int PLACE_PICKER_REQUEST = 15;

    Socket socket;

    Bitmap bitmap = null;

    private static final int GALLERY_REQUEST = 1;

    //TIMER

    boolean aktif;

    private long timeLeft;
    private CountDownTimer countDownTimer;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_konsultasi);

        mRvChat = findViewById(R.id.rv_chat);
        edtPesan = findViewById(R.id.edt_pesan);
        ibSend = findViewById(R.id.ib_send);
        tvDetik = findViewById(R.id.tv_detik);
        tvNama = findViewById(R.id.tv_nama);
        linearSend = findViewById(R.id.linear_send);
        linearTimer = findViewById(R.id.linear_timer);
        tb = findViewById(R.id.tb);
        swipeRefreshLayout = findViewById(R.id.swlayout);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nama = extras.getString("nama");
            room_id = extras.getString("room_id");
            jenis = extras.getString("jenis");
            notif_token = extras.getString("notif_token");
            tgl_transaksi = extras.getString("tgl_transaksi");
            status_chat = extras.getString("status_chat");

            tvNama.setText(nama);
        } else {
            tvNama.setText("-");
        }


        if (jenis == null) {
            jenis = "chat";
        }


        preferences = getSharedPreferences("prefs_timer" + room_id, MODE_PRIVATE);
        editor = preferences.edit();

        aktif = preferences.getBoolean("aktif", true);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        date = sdf.format(new Date());


        progressDialog = new ProgressDialog(this);
        preference = new UserPreferences(this);


        if (jenis.equals("riwayat") && !aktif) {
            linearSend.setVisibility(View.GONE);
            linearTimer.setVisibility(View.GONE);
        }

        if (status_chat.equals("N")) {
            linearSend.setVisibility(View.GONE);
            linearTimer.setVisibility(View.GONE);
        }

        getProfile();

        getListChat();

        tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListChat();
            }
        });

        edtPesan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPesan.getRight() - edtPesan.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showAttachDialog();
                        return true;
                    }
                }
                return false;
            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kirimPesan();

                sendNotificationToUser(notif_token == null ? Common.tokenNotif : notif_token);
            }
        });


        try {
            socket = IO.socket("https://apps-chat-halo-skin.herokuapp.com/");
            //socket = IO.socket("http://192.168.43.63:3000/");
            Log.d("Sukses", "Socket : " + socket.toString());
        } catch (URISyntaxException e) {
            Log.d("ErrorChat", "OnCreate : " + e.toString());
        }
        socket.connect();
        socket.on("pesan", handling);

        socket.emit("pesan", room_id, "aktif", "Hallo", date, "kosong", "user");


    }

    private Emitter.Listener handling = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (args[1].toString().equals("selesai")) {


                        editor.putBoolean("aktif", false);
                        editor.apply();
                        resetTimer();

                        sendNotifEndChat(notif_token);

                        Toast.makeText(ChatKonsultasiActiviy.this, "Sesi Konsultasi berakhir", Toast.LENGTH_SHORT).show();

                        finish();

                    } else {

                        if (args[4].toString().equals("produk")) {
                            getRekomendasiProduk(room_id);
                        } else if (args[4].toString().equals("halo")) {
                            Chat chat = new Chat();
                            chat.setId(args[0].toString());
                            chat.setNama(nama_profile);
                            chat.setPesan(args[2].toString());
                            chat.setTgl(args[3].toString());
                            chat.setJenis(args[4].toString());
                            chat.setSender(args[5].toString());
                            tambahPesan(chat);
                        } else {
                            Chat chat = new Chat();
                            chat.setId(args[0].toString());
                            chat.setNama("from_socket");
                            chat.setPesan(args[2].toString());
                            chat.setTgl(args[3].toString());
                            chat.setJenis(args[4].toString());
                            chat.setSender(args[5].toString());
                            tambahPesan(chat);
                        }

                    }


                }
            });
        }
    };

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

                if (status_chat.equals("Y")) {

                    new AlertDialog.Builder(ChatKonsultasiActiviy.this)
                            .setTitle("Perhatian!")
                            .setMessage("Sesi waktu konsultasi anda telah berakhir!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    socket.emit("pesan", room_id, "selesai", "", "", "user", "");

                                    editor.putBoolean("aktif", false);
                                    editor.apply();

                                    sendNotifEndChat(notif_token);

                                    resetTimer();

                                    setEndChat(room_id);

                                }
                            }).show();

                }

            }

        }.start();

    }

    private void timeLeftTimer() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date tglTransaksi = df.parse(tgl_transaksi);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String date = sdf.format(new Date());
            Date tglNow = df.parse(date);

            long diff = 1800000 - (tglNow.getTime() - tglTransaksi.getTime());

            timeLeft = diff;

            if (diff < 0) {
                linearSend.setVisibility(View.GONE);
                linearTimer.setVisibility(View.GONE);
            }

            Log.d("TIMERWAKTU", "" + tglTransaksi);
            Log.d("TIMERWAKTU", "" + tglNow);
            Log.d("TIMERWAKTU", "" + diff);

        } catch (ParseException e) {
            Log.d("TIMERWAKTU", "" + e.getMessage());
        }
    }

    public void updateTimer() {
        //int menit = (int) timeLeft / (1000*60) % 60;
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        Log.d("WAKTU", "" + timeLeftFormatted);
        tvDetik.setText(timeLeftFormatted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (aktif) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_chat, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_end:
                new AlertDialog.Builder(ChatKonsultasiActiviy.this)
                        .setTitle("Perhatian!")
                        .setMessage("Apakah Kamu Yakin Untuk Mengakhiri konsultasi?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                socket.emit("pesan", room_id, "selesai", "", "", "text", "user");

                                editor.putBoolean("aktif", false);
                                editor.apply();

                                sendNotifEndChat(notif_token);

                                setEndChat(room_id);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;

            case android.R.id.home:

                finish();
                break;

        }
        return true;
    }


    public void kirimPesan() {
        String pesan = edtPesan.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        date = sdf.format(new Date());

        addChat(pesan, date, nama, "text");
    }

    public void kirimPesanGambar(String foto) {
        String pesan = edtPesan.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        date = sdf.format(new Date());

        Pattern replace = Pattern.compile("\n");
        Matcher matcher2 = replace.matcher(foto);
        String fotoReplace = matcher2.replaceAll("");

        addChat(fotoReplace, date, nama, "image");
    }

    public void kirimPesanProduk(int idProduk, String nama, String fotoProduk, Integer quantity, Integer penggunaan, String jenis, String waktu) {

        ChatProduk chatProduk = new ChatProduk();
        Chat chat = new Chat();
        chat.setId("1");
        chat.setTgl(date);
        chat.setFoto(foto);
        chat.setJenis("produk");
        chatProduk.setImage(fotoProduk);
        chatProduk.setProduk_name(nama);
        chatProduk.setJumlah(String.valueOf(quantity));
        chatProduk.setId_produk(String.valueOf(idProduk));
        chatProduk.setPenggunaan(String.valueOf(penggunaan));
        chatProduk.setJenis(String.valueOf(jenis));
        chatProduk.setWaktu(String.valueOf(waktu));
        chat.setProduk(chatProduk);
        tambahPesan(chat);
    }

    private void tambahPesan(Chat chat) {
        listChat.add(chat);
        if (listChat != null) {
            adapterChat.notifyItemInserted(listChat.size() - 1);
            mRvChat.smoothScrollToPosition(listChat.size() - 1);
        }
    }

    private void setAdapter() {
        mRvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvChat.setHasFixedSize(true);
        adapterChat = new ChatAdapter(this, listChat, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int posisition) {

                Chat model = listChat.get(posisition);

                if (model.getJenis().equals("image")) {

                    showDialogImage(model.getNama(), model.getPesan());

                } else if (model.getJenis().equals("produk")) {
                    showDialogProduk(model.getProduk().getId_produk(), model.getProduk().getProduk_name(), model.getProduk().getImage(),
                            model.getProduk().getJumlah(), model.getProduk().getPenggunaan(), model.getProduk().getJenis(), model.getProduk().getWaktu());

                }

            }
        });
        mRvChat.setAdapter(adapterChat);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);

                foto = ImageUtils.bitmapToBase64String(bitmap, 50);

                kirimPesanGambar(foto);

            } catch (IOException e) {

                e.printStackTrace();
            }
        } else if (requestCode == 0) {

            bitmap = (Bitmap) data.getExtras().get("data");

            foto = ImageUtils.bitmapToBase64String(bitmap);

            kirimPesanGambar(foto);
        }


        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            int id_produk = data.getIntExtra("id", 0);
        }
    }

    private void getRekomendasiProduk(String id_transaksi) {

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> getData = api.getRekomendasi(preference.getEmail(), preference.getToken(), Base.apiToken, id_transaksi);
        getData.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {

                if (response.body() != null) {

                    List<Produk> listProduk = response.body().getData();

                    kirimPesanProduk(listProduk.get(0).getItem_id(), listProduk.get(0).getNama(), listProduk.get(0).getFoto(),
                            listProduk.get(0).getQuantity(), listProduk.get(0).getPenggunaan(), listProduk.get(0).getJenis(), listProduk.get(0).getWaktu());

                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                }

                //   progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                //    progressDialog.dismiss();
                Toast.makeText(ChatKonsultasiActiviy.this, "Network Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addChat(final String pesan, final String date, final String nama, final String type) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        stringItem.setLength(0);

        if (type.equals("produk")) {
            stringItem.append("{\"sender\":");
            stringItem.append("\"user\",");
            stringItem.append("\"type\":");
            stringItem.append("\"" + type + "\",");
            stringItem.append("\"message\":{\"produk_name\":\"skin care\",\"image\" : \"asdsa\",\"jumlah\" : \"2\"},");
            stringItem.append("\"" + pesan + "\",");
            stringItem.append("\"time\":");
            stringItem.append("\"" + date + "\"");
            stringItem.append("}");
        } else {
            stringItem.append("{\"sender\":");
            stringItem.append("\"user\",");
            stringItem.append("\"type\":");
            stringItem.append("\"" + type + "\",");
            stringItem.append("\"message\":");
            stringItem.append("\"" + pesan + "\",");
            stringItem.append("\"time\":");
            stringItem.append("\"" + date + "\"");
            stringItem.append("}");
        }


        chatKonsul = stringItem.toString();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ChatResponse> data = api.updateChat(preference.getEmail(), preference.getToken(), Base.apiToken, room_id, chatKonsul);
        data.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        edtPesan.setText("");

                        if (type.equals("text")) {

                            socket.emit("pesan", room_id, "aktif", pesan, date, "text", "user");

                        } else if (type.equals("image")) {

                            socket.emit("pesan", room_id, "aktif", pesan, date, "image", "user");

                        }


                    } else {

                    }

                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChatKonsultasiActiviy.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                        nama_profile = data.getNama();

                        socket.emit("pesan", room_id, "aktif", "Hallo", date, "halo", "user");


                    } else {
                        Toast.makeText(ChatKonsultasiActiviy.this, "Data Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChatKonsultasiActiviy.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addCart(String idBarang) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Toast.makeText(this, "" + idBarang, Toast.LENGTH_SHORT).show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ProdukResponse> data = api.addCart(preference.getEmail(), preference.getToken(), Base.apiToken, idBarang);
        data.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call, Response<ProdukResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Toast.makeText(ChatKonsultasiActiviy.this, "Berhasil Ditambahkan Keranjang!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ChatKonsultasiActiviy.this, "Barang Sudah Ditambahkan Di Keranjang!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "error null :" + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChatKonsultasiActiviy.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showDialogImage(String nama, String foto) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_zoom_gambar, null);

        final AlertDialog dialog = builder.create();

        ZoomageView ivFoto = v.findViewById(R.id.iv_foto);

        if (nama != null) {
            ivFoto.setImageBitmap(ImageUtils.base64toBitmap(foto));
        } else {
            Glide
                    .with(this)
                    .load(Base.baseImageChat + foto)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(ivFoto);
        }


        dialog.setView(v);

        dialog.show();
    }

    public void showDialogProduk(final String id_produk, String nama, String foto, String kuantitas, String penggunaan, String jenis, String waktu) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_rekomendasi_produk, null);

        final AlertDialog dialog = builder.create();

        ImageView ivFoto = v.findViewById(R.id.iv_foto);
        TextView tvNama = v.findViewById(R.id.tv_nama);
        Button btnKuantitas = v.findViewById(R.id.btn_kuantitas);
        Button btnPerhari = v.findViewById(R.id.btn_perhari);
        TextView tvWaktu = v.findViewById(R.id.tv_waktu);
        TextView tvJenis = v.findViewById(R.id.tv_jenis);
        Button btnKeranjang = v.findViewById(R.id.btn_keranjang);

        Glide
                .with(this)
                .load(Base.baseImageUrl + foto)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(ivFoto);

        tvNama.setText(nama);
        btnKuantitas.setText(kuantitas);
        btnPerhari.setText(penggunaan);
        tvJenis.setText(jenis);


        String time = waktu.replace(",", ":");

        Log.d("Waktu", time);

        String[] waktuGuna = time.split(":");

        StringBuilder stringWaktu = new StringBuilder();


        if (waktuGuna[1].equals(" 1")) {
            stringWaktu.append("Sebelum Makan,");
        }

        if (waktuGuna[3].equals(" 1")) {
            stringWaktu.append(" Sesudah Makan,");
        }

        if (waktuGuna[5].equals(" 1")) {
            stringWaktu.append(" Pagi,");
        }

        if (waktuGuna[7].equals(" 1")) {
            stringWaktu.append(" Siang,");
        }

        if (waktuGuna[9].equals(" 1")) {
            stringWaktu.append(" Sore,");
        }

        if (waktuGuna[11].equals(" 1")) {
            stringWaktu.append(" Malam,");
        }

        tvWaktu.setText(stringWaktu);

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(id_produk);
                dialog.dismiss();
            }
        });

        dialog.setView(v);

        dialog.show();
    }


    public void showAttachDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_attach, null);

        final AlertDialog dialog = builder.create();

        linearAttachCamera = v.findViewById(R.id.linear_attach_camera);
        linearAttachGallery = v.findViewById(R.id.linear_attach_gallery);

        linearAttachCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
                dialog.cancel();
            }
        });

        linearAttachGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                dialog.cancel();
            }
        });


        dialog.setView(v);

        dialog.show();
    }

    private void getListChat() {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<ChatResponse> getData = api.getListChat(preference.getEmail(), preference.getToken(), Base.apiToken, room_id);
        getData.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {

                if (response.body().getChat() != null) {

                    listChat = response.body().getChat().getData();

                    swipeRefreshLayout.setRefreshing(false);

                    if (listChat != null) {
                        setAdapter();
                    } else {
                        Toast.makeText(ChatKonsultasiActiviy.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);

                    }

                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ChatKonsultasiActiviy.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotificationToUser(String token) {

        DataModel data = new DataModel();
        data.setId_dokter("1");
        data.setNama(nama_profile);
        data.setId_room(room_id);
        data.setId_user("1");
        data.setId_pasien("1");

        String isi = edtPesan.getText().toString();

        Notification notif = new Notification();
        notif.setTitle(nama_profile);
        notif.setBody(isi);
        notif.setClick_action("chat");

        NotificationResponse rootModel = new NotificationResponse();
        rootModel.setToken(token);
        rootModel.setData(data);
        rootModel.setNotification(notif);

        ApiRequest apiService = RetrofitServer.getClientFcm().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void sendNotifEndChat(String token) {

        DataModel data = new DataModel();
        data.setId_dokter("1");
        data.setNama(nama_profile);
        data.setId_room(room_id);
        data.setId_user("1");
        data.setId_pasien("1");

        Notification notif = new Notification();
        notif.setTitle("Konsultasi Berakhir");
        notif.setBody("User mengakhiri sesi konsultasi");
        notif.setClick_action("chat");

        NotificationResponse rootModel = new NotificationResponse();
        rootModel.setToken(token);
        rootModel.setData(data);
        rootModel.setNotification(notif);

        ApiRequest apiService = RetrofitServer.getClientFcm().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setEndChat(final String room_id) {
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<StartChatResponse> getData = api.endChat(preference.getEmail(), preference.getToken(), Base.apiToken, room_id);
        getData.enqueue(new Callback<StartChatResponse>() {
            @Override
            public void onResponse(Call<StartChatResponse> call, Response<StartChatResponse> response) {

                if (response.body() != null) {

                    if (!response.body().getError()) {
                        Toast.makeText(ChatKonsultasiActiviy.this, "Sesi Konsultasi Berakhir!", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ChatKonsultasiActiviy.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ChatKonsultasiActiviy.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StartChatResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChatKonsultasiActiviy.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        resetTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        timeLeftTimer();

        startTimer();
    }


    private void resetTimer() {
        countDownTimer.cancel();

        timeLeft = 1800000;
        updateTimer();
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
