package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.solvedev.haloskin.R;


public class PembayaranKonsultasiActivity extends AppCompatActivity {

    private static String name, phone, email, address;
    private String name_produk;
    private Integer harga;

    WebView wvMidtrans;
    String url, nama;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_konsultasi);


        wvMidtrans = findViewById(R.id.webView);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pembayaran");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            nama = extras.getString("nama");
        }


        progressDialog = new ProgressDialog(this);

        wvMidtrans.loadUrl(url);
        wvMidtrans.getSettings().setJavaScriptEnabled(true);
        wvMidtrans.clearCache(true);
        wvMidtrans.clearHistory();
        wvMidtrans.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvMidtrans.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressDialog.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }
        });


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intentRegis = new Intent(PembayaranKonsultasiActivity.this, ChatKonsultasiActiviy.class);
//        intentRegis.putExtra("nama", nama);
//        startActivity(intentRegis);

        finish();
    }
}