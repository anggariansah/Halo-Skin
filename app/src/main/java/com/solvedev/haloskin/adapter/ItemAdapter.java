package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.HolderData> {

    private List<Produk> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnButtonTambahClickListener mOnButtonTambahClickListener;
    private OnButtonDeleteClickListener mOnButtonDeleteClickListener;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public ItemAdapter(Context ctx, List<Produk> mList, OnItemClickListener mOnItemClickListener, OnButtonTambahClickListener mOnButtonTambahClickListener,OnButtonDeleteClickListener mOnButtonDeleteClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnButtonTambahClickListener = mOnButtonTambahClickListener;
        this.mOnButtonDeleteClickListener = mOnButtonDeleteClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_cart, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, final int position) {
        Produk model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama());
        holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getHarga())));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnTambah.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                mOnButtonTambahClickListener.onButtonTambahClick(v, position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnTambah.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.GONE);
                mOnButtonDeleteClickListener.onButtonDeleteClick(v, position);
            }
        });


        Glide
                .with(ctx)
                .load(Base.baseImageUrl + model.getFoto())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.ivFoto);


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {

        private TextView tvNama, tvHarga;
        private Button btnTambah, btnDelete;
        private ImageView ivFoto;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvHarga = v.findViewById(R.id.tv_harga);
            btnTambah = v.findViewById(R.id.btn_tambah);
            btnDelete = v.findViewById(R.id.btn_hapus);
            ivFoto = v.findViewById(R.id.iv_foto);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

    public interface OnButtonTambahClickListener {
        void onButtonTambahClick(View v, int posisition);
    }

    public interface OnButtonDeleteClickListener {
        void onButtonDeleteClick(View v, int posisition);
    }
}
