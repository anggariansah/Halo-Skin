package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Produk;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.HolderData> {

    private List<Produk> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public KeranjangAdapter(Context ctx, List<Produk> mList, OnItemClickListener mOnItemClickListener,OnButtonClickListener mOnButtonClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnButtonClickListener = mOnButtonClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keranjang, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, final int position) {
        final Produk model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama());
        holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getHarga())));
        holder.btnJumlah.setNumber("1");
        model.setJumlah(1);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.btnJumlah.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                model.setJumlah(newValue);
                mOnButtonClickListener.onButtonClick(view, position, newValue);

              //  int jumlah  = model.getHarga() * newValue;

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
        private ImageView ivFoto;
        private ElegantNumberButton btnJumlah;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvHarga = v.findViewById(R.id.tv_harga);
            btnJumlah = v.findViewById(R.id.btn_jumlah);
            ivFoto = v.findViewById(R.id.iv_foto);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }


    public interface OnButtonClickListener {
        void onButtonClick(View v, int posisition, int jumlah);
    }

}
