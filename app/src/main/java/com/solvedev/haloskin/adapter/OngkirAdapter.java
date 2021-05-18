package com.solvedev.haloskin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.DataOngkir;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class OngkirAdapter extends RecyclerView.Adapter<OngkirAdapter.HolderData> {

    private List<DataOngkir> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private HolderData mHolder;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    public OngkirAdapter(Context ctx, List<DataOngkir> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ongkir, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HolderData holder, final int position) {

        DataOngkir model = mList.get(position);

        mHolder = holder;

        if(model.getCode().equals("jne")){
            holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getValue())));
            holder.tvHari.setText(model.getEtd());
            holder.ivFoto.setImageDrawable(ctx.getResources().getDrawable(R.drawable.jne));
            holder.tvNama.setText(model.getService());
        }else if(model.getCode().equals("J&T")){
            holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getValue())));
            holder.tvHari.setText(model.getEtd());
            holder.tvNama.setText(model.getService());
            holder.ivFoto.setImageDrawable(ctx.getResources().getDrawable(R.drawable.jnt));
        }else if(model.getCode().equals("pos")){
            holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getValue())));
            holder.tvHari.setText(model.getEtd());
            holder.tvNama.setText(model.getService());
            holder.ivFoto.setImageDrawable(ctx.getResources().getDrawable(R.drawable.pos));
        }else if(model.getCode().equals("tiki")){
            holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getValue())));
            holder.tvHari.setText(model.getEtd());
            holder.tvNama.setText(model.getService());
            holder.ivFoto.setImageDrawable(ctx.getResources().getDrawable(R.drawable.tiki));
        }else if(model.getCode().equals("sicepat")){
            holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getValue())));
            holder.tvHari.setText(model.getEtd());
            holder.tvNama.setText(model.getService());
            holder.ivFoto.setImageDrawable(ctx.getResources().getDrawable(R.drawable.sicepat));
        }


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {

        TextView tvNama, tvHarga, tvHari;
        ImageView ivFoto;
        RadioButton radioPilih;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvHarga = v.findViewById(R.id.tv_harga);
            tvHari = v.findViewById(R.id.tv_hari);
            ivFoto = v.findViewById(R.id.iv_foto);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

}
