package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.activity.ChatKonsultasiActiviy;
import com.solvedev.haloskin.model.Chat;
import com.solvedev.haloskin.model.User;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.ImageUtils;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.HolderData> {

    private List<Chat> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private UserPreferences preference;


    private HolderData mHolder;

    public ChatAdapter(Context ctx, List<Chat> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {

        Chat model = mList.get(position);

        preference = new UserPreferences(ctx);

        mHolder = holder;

        if (model != null) {


            if (model.getJenis().equals("halo")) {
                holder.linearChat.setVisibility(View.GONE);
                holder.cardHallo.setVisibility(View.VISIBLE);
                holder.cardGambar.setVisibility(View.GONE);
                holder.cardProduk.setVisibility(View.GONE);

                holder.tvNamaHalo.setText("Halo " + model.getNama() + " Selamat Datang, kamu bisa mengungkapkan keluhan mu disini!");

            } else if (model.getJenis().equals("image")) {

                holder.linearChat.setVisibility(View.GONE);
                holder.cardHallo.setVisibility(View.GONE);
                holder.cardGambar.setVisibility(View.VISIBLE);
                holder.cardProduk.setVisibility(View.GONE);

//            holder.ivFoto.setImageBitmap(ImageUtils.base64toBitmap(model.getFoto()));

                if (model.getNama() == null){
                    Glide
                            .with(ctx)
                            .load(Base.baseImageChat + model.getPesan())
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(holder.ivFoto);
                }else{

                    if(model.getNama().equals("from_socket")){
                        holder.ivFoto.setImageBitmap(ImageUtils.base64toBitmap(model.getPesan()));

                    }else{

                        Glide
                                .with(ctx)
                                .load(Base.baseImageChat + model.getPesan())
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(holder.ivFoto);
                    }
                }


            } else if (model.getJenis().equals("text")) {

                if(model.getSender().equals("dokter")){
                    holder.linearChat.setVisibility(View.VISIBLE);
                    holder.cardHallo.setVisibility(View.GONE);
                    holder.cardGambar.setVisibility(View.GONE);
                    holder.cardProduk.setVisibility(View.GONE);

                    holder.linearChat.setGravity(Gravity.START);

                    holder.tvDate.setText(model.getTgl());
                    holder.tvPesan.setText(model.getPesan());

                }else if(model.getSender().equals("user")){
                    holder.linearChat.setVisibility(View.VISIBLE);
                    holder.cardHallo.setVisibility(View.GONE);
                    holder.cardGambar.setVisibility(View.GONE);
                    holder.cardProduk.setVisibility(View.GONE);

                    holder.linearChat.setGravity(Gravity.END);

                    holder.tvDate.setText(model.getTgl());
                    holder.tvPesan.setText(model.getPesan());
                }

            } else if (model.getJenis().equals("produk")) {
                holder.linearChat.setVisibility(View.GONE);
                holder.cardHallo.setVisibility(View.GONE);
                holder.cardGambar.setVisibility(View.GONE);
                holder.cardProduk.setVisibility(View.VISIBLE);

                holder.linearChat.setGravity(Gravity.END);

                holder.tvDate.setText(model.getTgl());
                holder.tvPesan.setText(model.getPesan());

                holder.tvQty.setText("Qty : " + model.getProduk().getJumlah());
                holder.tvNamaProduk.setText(model.getProduk().getProduk_name());

                Glide
                        .with(ctx)
                        .load(Base.baseImageUrl + model.getProduk().getImage())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(holder.ivFotoProduk);

            }else{
                holder.linearChat.setVisibility(View.GONE);
                holder.cardHallo.setVisibility(View.GONE);
                holder.cardGambar.setVisibility(View.GONE);
                holder.cardProduk.setVisibility(View.GONE);
            }

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });

        }else{
            holder.linearChat.setVisibility(View.GONE);
            holder.cardHallo.setVisibility(View.GONE);
            holder.cardGambar.setVisibility(View.GONE);
            holder.cardProduk.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    class HolderData extends RecyclerView.ViewHolder {

        TextView tvPesan, tvDate, tvNamaHalo, tvNamaProduk, tvQty;
        LinearLayout linearChat;
        CardView cardHallo, cardGambar, cardProduk;
        ImageView ivFoto, ivFotoProduk;

        public View container;


        public HolderData(View v) {
            super(v);

            container = v;

            tvPesan = v.findViewById(R.id.tv_pesan);
            tvDate = v.findViewById(R.id.tv_date);
            linearChat = v.findViewById(R.id.linear_chat);
            cardHallo = v.findViewById(R.id.card_hello);
            cardGambar = v.findViewById(R.id.card_gambar);
            cardProduk = v.findViewById(R.id.card_produk);
            ivFoto = v.findViewById(R.id.iv_foto);
            tvNamaHalo = v.findViewById(R.id.tv_nama_halo);
            tvNamaProduk = v.findViewById(R.id.tv_nama_produk);
            tvQty = v.findViewById(R.id.tv_qty);
            ivFotoProduk = v.findViewById(R.id.iv_foto_produk);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }


//    private void get() {
//        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
//        Call<UserRespons> login = api.getProfile(preference.getEmail(), preference.getToken(), Base.apiToken);
//        login.enqueue(new Callback<UserRespons>() {
//            @Override
//            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
//
//                if(response.body() != null){
//
//                    boolean error = response.body().getError();
//
//                    if (!error) {
//
//                        User data = response.body().getData();
//
//
//                    } else {
//                        Toast.makeText(ctx, "Data Tidak Ditemukan!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }else{
//                    Toast.makeText(ctx, "error null", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<UserRespons> call, Throwable t) {
//                Toast.makeText(ctx, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
