package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Kategori;

import java.util.List;

public class KategoriJenisAdapter extends RecyclerView.Adapter<KategoriJenisAdapter.HolderData> {

    private List<Kategori> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private HolderData mHolder;

    public KategoriJenisAdapter(Context ctx, List<Kategori> mList, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        Kategori model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama_jenis());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
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

        private TextView tvNama;
        private ImageView ivIcon;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            ivIcon = v.findViewById(R.id.iv_icon);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }
}
