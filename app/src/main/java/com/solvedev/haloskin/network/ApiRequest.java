package com.solvedev.haloskin.network;


import com.solvedev.haloskin.model.CekPromoResponse;
import com.solvedev.haloskin.model.ChatResponse;
import com.solvedev.haloskin.model.DetailDokterResponse;
import com.solvedev.haloskin.model.DokterResponse;
import com.solvedev.haloskin.model.KategoriResponse;
import com.solvedev.haloskin.model.KonsulDetailResponse;
import com.solvedev.haloskin.model.NotesResponse;
import com.solvedev.haloskin.model.NotificationResponse;
import com.solvedev.haloskin.model.OngkirResponse;
import com.solvedev.haloskin.model.ProdukResponse;
import com.solvedev.haloskin.model.PromoResponse;
import com.solvedev.haloskin.model.ResepResponse;
import com.solvedev.haloskin.model.RiwayatKonsulResponse;
import com.solvedev.haloskin.model.StartChatResponse;
import com.solvedev.haloskin.model.TransaksiResponse;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.utils.Base;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequest {


    @FormUrlEncoded
    @POST("register")
    Call<UserRespons> registerGmail(@Field("nama") String nama,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("user_type") String type);

    @FormUrlEncoded
    @POST("register")
    Call<UserRespons> register(@Field("email") String email,
                               @Field("password") String password,
                               @Field("nama") String nama,
                               @Field("tanggal_lahir") String tanggal_lahir,
                               @Field("tempat_lahir") String tempat_lahir,
                               @Field("jenis_kelamin") String jenis_kelamin,
                               @Field("kota") String kota,
                               @Field("alamat") String alamat,
                               @Field("nomor_wa") String no_wa,
                               @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("login/user")
    Call<UserRespons> login(@Field("email") String email,
                            @Field("password") String password,
                            @Field("mode") String mode,
                            @Field("user_type") String type,
                            @Header("apiToken") String token);

    @FormUrlEncoded
    @POST("login/user")
    Call<UserRespons> loginGoogle(@Field("email") String email,
                                  @Field("mode") String mode,
                                  @Field("user_type") String type,
                                  @Header("apiToken") String token);

    @FormUrlEncoded
    @POST("transaksi")
    Call<UserRespons> transaksiProduk(@Header("email") String email,
                                      @Header("token") String token,
                                      @Header("apiToken") String apiToken,
                                      @Field("listItem") String list);

    @FormUrlEncoded
    @POST("konsul")
    Call<KonsulDetailResponse> transaksiKonsul(@Header("email") String email,
                                               @Header("token") String token,
                                               @Header("apiToken") String apiToken,
                                               @Field("listItem") String list);


    @GET("profile/user")
    Call<UserRespons> getProfile(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken);

    @GET("dokter")
    Call<DokterResponse> getListDokter(@Header("email") String email,
                                       @Header("token") String token,
                                       @Header("apiToken") String apiToken);

    @GET("promo_list")
    Call<PromoResponse> getPromoList(@Header("email") String email,
                                     @Header("token") String token,
                                     @Header("apiToken") String apiToken);

    @GET("get_list_search/{nama}")
    Call<DokterResponse> getSearchDokter(@Header("email") String email,
                                         @Header("token") String token,
                                         @Header("apiToken") String apiToken,
                                         @Path("nama") String nama);

    @GET("item/{idtype}/{idjenis}")
    Call<ProdukResponse> getListProduk(@Header("email") String email,
                                       @Header("token") String token,
                                       @Header("apiToken") String apiToken,
                                       @Path("idtype") int idtype,
                                       @Path("idjenis") int idjenis);

    @GET("item/{idtype}/{idjenis}")
    Call<ProdukResponse> getListProdukResep(@Header("email") String email,
                                            @Header("token") String token,
                                            @Header("apiToken") String apiToken,
                                            @Path("idtype") int idtype,
                                            @Path("idjenis") int idjenis);

    @GET("get_list_produk/{idtype}/{idjenis}/{nama}")
    Call<ProdukResponse> getSearchProduk(@Header("email") String email,
                                         @Header("token") String token,
                                         @Header("apiToken") String apiToken,
                                         @Path("idtype") int idtype,
                                         @Path("idjenis") int idjenis,
                                         @Path("nama") String nama);

    @GET("item")
    Call<KategoriResponse> getTypeList(@Header("email") String email,
                                       @Header("token") String token,
                                       @Header("apiToken") String apiToken);

    @GET("item/{idtype}")
    Call<KategoriResponse> getJenisList(@Header("email") String email,
                                        @Header("token") String token,
                                        @Header("apiToken") String apiToken,
                                        @Path("idtype") int idType);

    @FormUrlEncoded
    @POST("verify/user")
    Call<UserRespons> verifiyCode(@Field("email") String email,
                                  @Field("verify_code") String verify_code,
                                  @Field("notification_token") String notification_token,
                                  @Header("apiToken") String apiToken);

    @FormUrlEncoded
    @POST("check_promo")
    Call<CekPromoResponse> checkPromo(@Header("email") String email,
                                      @Header("token") String token,
                                      @Header("apiToken") String apiToken,
                                      @Field("promo_code") String promoCode);

    @GET("getFavorites")
    Call<ProdukResponse> getFavorite(@Header("email") String email,
                                     @Header("token") String token,
                                     @Header("apiToken") String apiToken);

    @FormUrlEncoded
    @POST("setFavorites")
    Call<ProdukResponse> addFavorite(@Header("email") String email,
                                     @Header("token") String token,
                                     @Header("apiToken") String apiToken,
                                     @Field("id_barang") String idBarang);

    @FormUrlEncoded
    @POST("delFavorites")
    Call<ProdukResponse> deleteFavorite(@Header("email") String email,
                                        @Header("token") String token,
                                        @Header("apiToken") String apiToken,
                                        @Field("id_barang") String idBarang);

    @FormUrlEncoded
    @POST("setCart")
    Call<ProdukResponse> addCart(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken,
                                 @Field("id_barang") String idBarang);

    @FormUrlEncoded
    @POST("delCart")
    Call<ProdukResponse> deleteCart(@Header("email") String email,
                                    @Header("token") String token,
                                    @Header("apiToken") String apiToken,
                                    @Field("id_barang") String idBarang);

    @GET("getCart")
    Call<ProdukResponse> getCart(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken);



    @FormUrlEncoded
    @POST("update_profile/user")
    Call<UserRespons> updateProfile(@Field("nama") String nama,
                                    @Field("tanggal_lahir") String tanggal_lahir,
                                    @Field("nomor_wa") String no_wa,
                                    @Field("umur") String umur,
                                    @Field("is_hamil") String hamil,
                                    @Field("jenis_kulit") String jenis_kulit,
                                    @Field("is_kulit_sensitif") String kulit_sensitif,
                                    @Field("is_kulit_iritasi") String kulit_iritasi,
                                    @Field("is_kulit_berjerawat") String kulit_berjerawat,
                                    @Field("tingkat_jerawat") String tingkat_jerawat,
                                    @Field("keluhan") String keluhan,
                                    @Field("keluhan_lain") String keluhan_lain,
                                    @Field("jumlah_minum") String jumlah_minum,
                                    @Field("jumlah_sayuran") String jumlah_sayuran,
                                    @Field("jumlah_makanan_minyak") String jumlah_makanan_minyak,
                                    @Field("tempat_lahir") String tempat_lahir,
                                    @Field("jenis_kelamin") String jenis_kelamin,
                                    @Field("kota") String kota,
                                    @Field("alamat") String alamat,
                                    @Header("email") String email,
                                    @Header("token") String token,
                                    @Header("apiToken") String api_token);


    @FormUrlEncoded
    @POST("forgotPassword")
    Call<UserRespons> sendCode(@Field("email") String email,
                            @Header("apiToken") String token);

    @FormUrlEncoded
    @POST("verifyForgotPassword")
    Call<UserRespons> verifyCode(@Field("email") String email,
                                 @Field("verify_code") String code,
                                 @Header("apiToken") String token);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<UserRespons> updatePassword(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("cPassword") String cpassword,
                                 @Field("verify_code") String code,
                                 @Header("apiToken") String token);


    //Raja Ongkir

    @GET("province")
    Call<OngkirResponse> getProvince(@Header("key") String apikey);

    @GET("city")
    Call<OngkirResponse> getCity(@Header("key") String apikey,
                                 @Query("province") String province);

    @GET("subdistrict")
    Call<OngkirResponse> getKecamatan(@Header("key") String apikey,
                                      @Query("city") String city);

    @FormUrlEncoded
    @POST("cost")
    Call<OngkirResponse> getCost(@Header("key") String apikey,
                                 @Field("origin") String origin,
                                 @Field("originType") String originType,
                                 @Field("destination") String destination,
                                 @Field("destinationType") String destinationType,
                                 @Field("weight") String weight,
                                 @Field("courier") String courier);

    @GET("transaksi_list")
    Call<TransaksiResponse> getRiwayatBeli(@Header("email") String email,
                                         @Header("token") String token,
                                         @Header("apiToken") String apiToken);

    @GET("transaksi_detail/{idtransaksi}")
    Call<TransaksiResponse> getDetailRiwayat(@Header("email") String email,
                                           @Header("token") String token,
                                           @Header("apiToken") String apiToken,
                                           @Path("idtransaksi") String idtransaksi);


    @FormUrlEncoded
    @POST("setResep")
    Call<ResepResponse> addResep(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken,
                                 @Field("id_produk") String id_produk,
                                 @Field("resep") String resep);


    @GET("item_resep/3/1")
    Call<ResepResponse> getResep(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken);



    @GET("konsul_list/user")
    Call<RiwayatKonsulResponse> getRiwayatKonsul(@Header("email") String email,
                                                 @Header("token") String token,
                                                 @Header("apiToken") String apiToken);

    @GET("getRecItem/user/{id_trans}")
    Call<ProdukResponse> getRekomendasi(@Header("email") String email,
                                        @Header("token") String token,
                                        @Header("apiToken") String apiToken,
                                        @Path("id_trans") String id_trans);



    @FormUrlEncoded
    @POST("updateChat/user")
    Call<ChatResponse> updateChat(@Header("email") String email,
                                  @Header("token") String token,
                                  @Header("apiToken") String apiToken,
                                  @Header("roomId") String room_id,
                                  @Field("chat") String chat);

    @Headers({"Authorization: key=" + Base.SERVER_KEY_FCM, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationResponse root);

    @GET("getFullChat/user")
    Call<ChatResponse> getListChat(@Header("email") String email,
                                   @Header("token") String token,
                                   @Header("apiToken") String apiToken,
                                   @Header("roomId") String room_id);

    @GET("getFullNotes/user")
    Call<NotesResponse> getNotes(@Header("email") String email,
                                 @Header("token") String token,
                                 @Header("apiToken") String apiToken,
                                 @Header("roomId") String room_id);

    @GET("getDetailDokter/{iddokter}")
    Call<DetailDokterResponse> getDokterDetail(@Header("email") String email,
                                               @Header("token") String token,
                                               @Header("apiToken") String apiToken,
                                               @Path("iddokter") String iddokter);

    @FormUrlEncoded
    @POST("setChatStart/user")
    Call<StartChatResponse> startChat(@Header("email") String email,
                                      @Header("token") String token,
                                      @Header("apiToken") String apiToken,
                                      @Field("no_invoice") String no_invoice);


    @FormUrlEncoded
    @POST("setChatEnd/user")
    Call<StartChatResponse> endChat(@Header("email") String email,
                                    @Header("token") String token,
                                    @Header("apiToken") String apiToken,
                                    @Field("no_invoice") String no_invoice);




}
