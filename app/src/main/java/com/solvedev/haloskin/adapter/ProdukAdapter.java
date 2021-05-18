package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.Produk;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.HolderData> {

    private List<Produk> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private OnMinusClickListener mOnMinusClickListener;
    private OnPlusClickListener mOnPlusClickListener;

    int jumlah = 0;

    private HolderData mHolder;

    public ProdukAdapter(Context ctx, List<Produk> mList, OnItemClickListener mOnItemClickListener, OnButtonClickListener mOnButtonClickListener, OnMinusClickListener mOnMinusClickListener, OnPlusClickListener mOnPlusClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnButtonClickListener = mOnButtonClickListener;
        this.mOnMinusClickListener = mOnMinusClickListener;
        this.mOnPlusClickListener = mOnPlusClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, final int position) {
        Produk model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getNama());
        holder.tvHarga.setText(String.valueOf(model.getHarga()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlah = jumlah + 1;

                holder.tvJumlah.setText(String.valueOf(jumlah));

                holder.linTambah.setVisibility(View.VISIBLE);
                holder.btnTambah.setVisibility(View.GONE);

                mOnButtonClickListener.onButtonClick(v, position, jumlah);
            }
        });

        holder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlah = jumlah - 1;

                if(jumlah == 0){
                    holder.linTambah.setVisibility(View.GONE);
                    holder.btnTambah.setVisibility(View.VISIBLE);
                }

                holder.tvJumlah.setText(String.valueOf(jumlah));

                mOnMinusClickListener.onMinusClick(v, position, jumlah);
            }
        });

        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlah = jumlah + 1;

                holder.tvJumlah.setText(String.valueOf(jumlah));

                mOnPlusClickListener.onPlusClick(v, position, jumlah);
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

        private TextView tvNama, tvHarga, tvJumlah;
        private Button btnTambah;
        private ImageButton ibMinus, ibPlus;
        private ImageView ivFoto;
        private LinearLayout linTambah;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama);
            tvHarga = v.findViewById(R.id.tv_harga);
            tvJumlah = v.findViewById(R.id.tv_jumlah);
            btnTambah = v.findViewById(R.id.btn_tambah);
            ibMinus = v.findViewById(R.id.ib_minus);
            ibPlus = v.findViewById(R.id.ib_plus);
            ivFoto = v.findViewById(R.id.iv_foto);
            linTambah = v.findViewById(R.id.linear_tambah);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v, int posisition,int jumlah);
    }

    public interface OnMinusClickListener {
        void onMinusClick(View v, int posisition, int jumlah);
    }

    public interface OnPlusClickListener {
        void onPlusClick(View v, int posisition, int jumlah);
    }
}
