package com.solvedev.haloskin.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer {

    public static final String base_url = "https://gudangpratista.com/apps/api/";
    public static final String raja_ongkir_url = "https://pro.rajaongkir.com/api/";
    public static final String fcm_url = "https://fcm.googleapis.com/";

    private static Retrofit retrofit;
    private static Retrofit retrofitRajaOngkir;
    private static Retrofit retrofitFcm;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getClientRajaOngkir() {
        if (retrofitRajaOngkir == null) {
            retrofitRajaOngkir = new Retrofit.Builder()
                    .baseUrl(raja_ongkir_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitRajaOngkir;
    }

    public static Retrofit getClientFcm() {
        if (retrofitFcm == null) {
            retrofitFcm = new Retrofit.Builder()
                    .baseUrl(fcm_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitFcm;
    }

}
