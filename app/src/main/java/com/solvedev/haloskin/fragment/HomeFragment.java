package com.solvedev.haloskin.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.activity.AlamatPengirimanActivity;
import com.solvedev.haloskin.activity.BuyProdukActivity;

import com.solvedev.haloskin.activity.DaftarAlamatActivity;
import com.solvedev.haloskin.activity.KeranjangActivity;
import com.solvedev.haloskin.activity.LengkapiProfilActivity;
import com.solvedev.haloskin.activity.ListDokterActivity;
import com.solvedev.haloskin.activity.UbahLokasi;
import com.solvedev.haloskin.activity.UpdateProfileActivity;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CardView cvKonsultasi, cvBeli;
    private TextView tvName, tvAlamat;

    private UserPreferences preference;
    private ProgressDialog progressDialog;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMapG;
    private MapView mMapView;
    private Location currentLocation;
    private LatLng startLat;

    private static final int LOKASI_REQUEST = 15;

    String alamat;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvKonsultasi = view.findViewById(R.id.cv_konsultasi);
        cvBeli = view.findViewById(R.id.cv_beli);
        tvName = view.findViewById(R.id.tv_name);
        tvAlamat = view.findViewById(R.id.tv_alamat);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        progressDialog = new ProgressDialog(getActivity());
        preference = new UserPreferences(getActivity());

        getProfile(preference.getEmail());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Harap Aktifkan Lokasi Anda", Toast.LENGTH_LONG).show();
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
                            tvAlamat.setText(getStringAddress(center.latitude, center.longitude));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        cvBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyProdukActivity.class);
                startActivity(intent);
            }
        });

        cvKonsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListDokterActivity.class);
                startActivity(intent);
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        tvAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), UbahLokasi.class);
                startActivityForResult(a, LOKASI_REQUEST);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOKASI_REQUEST && resultCode == Activity.RESULT_OK) {
            alamat = data.getStringExtra("alamat");

            tvAlamat.setText(alamat);
        }
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
        return;

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

                if(response.body() != null){

                    boolean error = response.body().getError();

                    if (!error) {

                        User data = response.body().getData();

                        tvName.setText(data.getNama());

                    } else {
                        Toast.makeText(getActivity(), "Data Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getActivity(), "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getStringAddress(Double lat, Double lng) {
        String address = "";
        String city = "";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
