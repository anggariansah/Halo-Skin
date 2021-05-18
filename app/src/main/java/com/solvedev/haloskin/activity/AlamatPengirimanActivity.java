package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.adapter.OngkirAdapter;
import com.solvedev.haloskin.database.DatabaseHelperHaloskin;
import com.solvedev.haloskin.model.Ongkir;
import com.solvedev.haloskin.model.OngkirResponse;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlamatPengirimanActivity extends AppCompatActivity {

    private Spinner spProvinsi, spKota, spKecamatan, spJenis;
    private RecyclerView mRvCost;
    private OngkirAdapter adapterCost;
    private EditText edtAlamat, edtCatatan;
    private Button btnPilihAlamat;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    private List<Ongkir> listProvince, listCity, listKecamatan, listCost;
    private List<String> listJenis;
    private String kodeProvinsi, kodeKota, kodeKecamatan;

    private GoogleMap mMapG;
    Marker currentMarker;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng startLat;

    double latitude, longitude;

    SQLiteDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_pengiriman);

        spProvinsi = findViewById(R.id.sp_provinsi);
        spKota = findViewById(R.id.sp_kota);
        spKecamatan = findViewById(R.id.sp_kecamatan);
        spJenis = findViewById(R.id.sp_jenis);
        mRvCost = findViewById(R.id.rv_cost);
        edtAlamat = findViewById(R.id.edt_alamat);
        edtCatatan = findViewById(R.id.edt_catatan);
        btnPilihAlamat = findViewById(R.id.btn_pilih_alamat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pilih Alamat");
        }

        progressDialog = new ProgressDialog(this);

        tampilProvinsi();
        setMaps();
        setSpJenis();


        spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kodeProvinsi = listProvince.get(position).getProvince_id();
                tampilKota(kodeProvinsi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kodeKota = listCity.get(position).getCity_id();
                tampilKecamatan(kodeKota);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kodeKecamatan = listKecamatan.get(position).getSubdistrict_id();
                Toast.makeText(AlamatPengirimanActivity.this, ""+kodeKecamatan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPilihAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpan();
            }
        });


    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setSpJenis(){
        listJenis = new ArrayList<>();

        listJenis.add("Kantor");
        listJenis.add("Rumah");
        listJenis.add("Lainnya");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AlamatPengirimanActivity.this, android.R.layout.simple_spinner_dropdown_item, listJenis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenis.setAdapter(adapter);
    }

    public void tampilProvinsi() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClientRajaOngkir().create(ApiRequest.class);
        Call<OngkirResponse> getData = api.getProvince(Base.apiKeyRajaOngkir);
        getData.enqueue(new Callback<OngkirResponse>() {
            @Override
            public void onResponse(Call<OngkirResponse> call, Response<OngkirResponse> response) {

                listProvince = response.body().getRajaongkir().getResults();

                List<String> listSpinner = new ArrayList<>();

                for (int i = 0; i < listProvince.size(); i++) {
                    listSpinner.add(listProvince.get(i).getProvince());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AlamatPengirimanActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProvinsi.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OngkirResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AlamatPengirimanActivity.this, "Harap Periksa Koneksi Anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tampilKota(String kodeProvinsi) {
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClientRajaOngkir().create(ApiRequest.class);
        Call<OngkirResponse> getData = api.getCity(Base.apiKeyRajaOngkir, kodeProvinsi);
        getData.enqueue(new Callback<OngkirResponse>() {
            @Override
            public void onResponse(Call<OngkirResponse> call, Response<OngkirResponse> response) {

                listCity = response.body().getRajaongkir().getResults();

                List<String> listSpinner = new ArrayList<>();

                for (int i = 0; i < listCity.size(); i++) {
                    listSpinner.add(listCity.get(i).getType()  + " " + listCity.get(i).getCity_name());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlamatPengirimanActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spKota.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OngkirResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AlamatPengirimanActivity.this, "Harap Periksa Koneksi Anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tampilKecamatan(String kodeKota) {
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiRequest api = RetrofitServer.getClientRajaOngkir().create(ApiRequest.class);
        Call<OngkirResponse> getData = api.getKecamatan(Base.apiKeyRajaOngkir, kodeKota);
        getData.enqueue(new Callback<OngkirResponse>() {
            @Override
            public void onResponse(Call<OngkirResponse> call, Response<OngkirResponse> response) {

                listKecamatan = response.body().getRajaongkir().getResults();

                List<String> listSpinner = new ArrayList<>();

                for (int i = 0; i < listKecamatan.size(); i++) {
                    listSpinner.add(listKecamatan.get(i).getSubdistrict_name());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AlamatPengirimanActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spKecamatan.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OngkirResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AlamatPengirimanActivity.this, "Harap Periksa Koneksi Anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void simpan(){

        database = new DatabaseHelperHaloskin(this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alamat",edtAlamat.getText().toString());
        contentValues.put("jenis",spJenis.getSelectedItem().toString());
        contentValues.put("id_kecamatan",kodeKecamatan);
        contentValues.put("id_kota",kodeKota);
        contentValues.put("id_provinsi",kodeProvinsi);
        contentValues.put("lat",latitude);
        contentValues.put("lng",longitude);
        contentValues.put("catatan",edtCatatan.getText().toString());

        long insert =  database.insert("tb_alamat", null, contentValues);

        if(insert != -1){
            Toast.makeText(this, "Berhasil Menambahkan Data", Toast.LENGTH_SHORT).show();
            Intent intentFavorite = new Intent(AlamatPengirimanActivity.this, DaftarAlamatActivity.class);
            startActivity(intentFavorite);
            finish();

        }else{
            Toast.makeText(this, "Penyimpanan Gagal", Toast.LENGTH_SHORT).show();
            finish();

        }

        database.close();

    }

    private void setMaps(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AlamatPengirimanActivity.this, "Harap Aktifkan Lokasi Anda", Toast.LENGTH_LONG).show();
                    return;
                }

                mMapG = mMap;
                mMapG.setMyLocationEnabled(true);
                fetchLastLocation(mMap);

                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        try {
                            LatLng center = mMap.getCameraPosition().target;

                            latitude = center.latitude;
                            longitude = center.longitude;

                            edtAlamat.setText(getStringAddress(center.latitude, center.longitude));
                        } catch (Exception e) {

                        }
                    }
                });
            }
        });
    }

    private void fetchLastLocation(final GoogleMap mMap) {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    startLat = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(startLat)
                            .zoom(17)
                            .bearing(0)
                            .tilt(45)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);

                }
            }
        });

    }

    public String getStringAddress(Double lat, Double lng) {
        String address = "";
        String city = "";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address + " " + city;

    }

}