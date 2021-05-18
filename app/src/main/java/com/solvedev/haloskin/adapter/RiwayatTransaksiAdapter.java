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
import com.solvedev.haloskin.model.Transaksi;
import com.solvedev.haloskin.utils.RupiahConvert;

import java.util.List;

public class RiwayatTransaksiAdapter extends RecyclerView.Adapter<RiwayatTransaksiAdapter.HolderData> {

    private List<Transaksi> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnButtonClickListener mOnButtonClickListener;

    private RupiahConvert rupiahConvert = new RupiahConvert();

    private HolderData mHolder;

    public RiwayatTransaksiAdapter(Context ctx, List<Transaksi> mList, OnItemClickListener mOnItemClickListener, OnButtonClickListener mOnButtonClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnButtonClickListener = mOnButtonClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pembelian_produk, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        Transaksi model = mList.get(position);

        mHolder = holder;

        holder.tvNama.setText(model.getName());
        holder.tvNoPesanan.setText(model.getNo_invoice());
        holder.tvJumlah.setText(model.getQuantity() + " Pcs");
        holder.tvHarga.setText(rupiahConvert.convertStringToRupiah(String.valueOf(model.getPrice())));
        holder.tvNoResi.setText("Resi : "+ model.getNo_resi());
        holder.tvTanggal.setText(model.getTgl_transaksi());

//        if (model.getName().equals("Ongkos Kirim")){
//            holder.container.setVisibility(View.GONE);
//        }


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClick(v, position);
            }
        });

//
//        Glide
//                .with(ctx)
//                .load(RetrofitServer.base_url + model.getList_item().get(position).get)
//                .centerCrop()
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.ivFoto);


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {

        private TextView tvNama, tvJumlah, tvNoResi,tvHarga,tvTanggal,tvNoPesanan, tvOngkir;
        private Button btnBeli;
        private ImageView ivFoto;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvNama = v.findViewById(R.id.tv_nama_produk);
            tvJumlah = v.findViewById(R.id.tv_jumlah_item);
            tvNoResi = v.findViewById(R.id.tv_no_resi_item);
            tvHarga = v.findViewById(R.id.tv_harga_item);
            tvTanggal = v.findViewById(R.id.tv_tanggal_item);
            tvNoPesanan = v.findViewById(R.id.tv_no_pesanan_item);
            tvOngkir = v.findViewById(R.id.tv_ongkos_kirim);
            btnBeli = v.findViewById(R.id.btn_beli);
            ivFoto = v.findViewById(R.id.iv_foto_item);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

    public interface OnButtonClickListener {
        void onButtonClick(View v, int posisition);
    }



}
