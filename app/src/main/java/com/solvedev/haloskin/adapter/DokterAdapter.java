package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Dokter;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class DokterAdapter extends RecyclerView.Adapter<DokterAdapter.HolderData> {

    private List<Dokter> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public DokterAdapter(Context ctx, List<Dokter> mList, OnItemClickListener mOnItemClickListener, OnButtonClickListener mOnButtonClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnButtonClickListener = mOnButtonClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dokter, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        Dokter model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama());
        holder.tvJenisDokter.setText(model.getJenis_dokter());
        holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getHarga())));
        holder.tvPengalaman.setText(model.getPengalaman() + " Tahun");

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

        private TextView tvNama, tvJenisDokter, tvHarga, tvPengalaman;
        private Button btnChat;
        private ImageView ivFoto;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvJenisDokter = v.findViewById(R.id.tv_jenis_dokter);
            tvHarga = v.findViewById(R.id.tv_harga);
            tvPengalaman = v.findViewById(R.id.tv_pengalaman);
            btnChat = v.findViewById(R.id.btn_chat);
            ivFoto = v.findViewById(R.id.iv_foto);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v, int posisition);
    }
}
