package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.RiwayatKonsultasi;

import java.util.List;

public class RiwayatKonsulAdapter extends RecyclerView.Adapter<RiwayatKonsulAdapter.HolderData> {

    private List<RiwayatKonsultasi> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnNotesClickListener mOnNotesClicikListener;
    private OnButtonClickListener mOnButtonClickListener;

    private HolderData mHolder;

    public RiwayatKonsulAdapter(Context ctx, List<RiwayatKonsultasi> mList, OnItemClickListener mOnItemClickListener, OnNotesClickListener mOnNotesClicikListener, OnButtonClickListener mOnButtonClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnNotesClicikListener = mOnNotesClicikListener;
        this.mOnButtonClickListener = mOnButtonClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_chat, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        RiwayatKonsultasi model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getList_item().get(0).getName());
        holder.tvTgl.setText(model.getTgl());

//        holder.tvJenisDokter.setText(model.getJenis_dokter());
//        holder.tvTgl.setText(model.getTgl());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClick(v, position);
            }
        });

        holder.tvCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnNotesClicikListener.onNotesClick(v, position);
            }
        });


//        Glide
//                .with(ctx)
//                .load(RetrofitServer.base_url + model.get())
//                .centerCrop()
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.ivFoto);


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {

        private TextView tvNama, tvJenisDokter, tvTgl, tvCatatan;
        private Button btnChat;
        private ImageView ivFoto;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvJenisDokter = v.findViewById(R.id.tv_jenis_dokter);
            tvTgl = v.findViewById(R.id.tv_tanggal);
            btnChat = v.findViewById(R.id.btn_chat_ulang_item);
            ivFoto = v.findViewById(R.id.iv_foto);
            tvCatatan = v.findViewById(R.id.tv_catatan);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }
    public interface OnNotesClickListener {
        void onNotesClick(View v, int posisition);
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v, int posisition);
    }
}
