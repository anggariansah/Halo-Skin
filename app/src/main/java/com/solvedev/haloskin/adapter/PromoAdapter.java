package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Promo;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.HolderData> {

    private List<Promo> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public PromoAdapter(Context ctx, List<Promo> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo_tersedia, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        Promo model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama_promo());
        holder.tvKode.setText("Kode Promo : "+ model.getKode_promo());

        if (model.getType().equals("nominal")){
            holder.tvNominal.setText("Nominal : "+rupiahConvert.convertStringToRupiah(String.valueOf(model.getNominal())));
        }else if(model.getType().equals("precentage")){
            holder.tvNominal.setText("Nominal : "+ model.getNominal() + " %");
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

        private TextView tvNama, tvKode, tvNominal;


        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvKode = v.findViewById(R.id.tv_kode);
            tvNominal = v.findViewById(R.id.tv_nominal);


        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

}
