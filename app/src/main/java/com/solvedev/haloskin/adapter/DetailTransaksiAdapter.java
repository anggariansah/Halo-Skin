package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Transaksi;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class DetailTransaksiAdapter  extends RecyclerView.Adapter<DetailTransaksiAdapter.HolderData> {

    private List<Transaksi> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public DetailTransaksiAdapter(Context ctx, List<Transaksi> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_transaksi, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        Transaksi model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getName());
        holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getPrice())));
        holder.tvJumlah.setText(model.getQuantity());
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

        private TextView tvNama, tvHarga, tvJumlah;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvHarga = v.findViewById(R.id.tv_harga);
            tvJumlah = v.findViewById(R.id.tv_jumlah);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }
}
