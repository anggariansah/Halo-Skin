package com.solvedev.haloskin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.DaftarAlamat;
import java.util.List;

public class DaftarAlamatAdapter extends RecyclerView.Adapter<DaftarAlamatAdapter.HolderData> {

    private List<DaftarAlamat> mList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnEditButtonClickListener mOnEditButtonClickListener;
    private OnDeleteButtonClickListener mOnDeleteButtonClickListener;

    private HolderData mHolder;

    public DaftarAlamatAdapter(Context ctx, List<DaftarAlamat> mList, OnItemClickListener mOnItemClickListener, OnEditButtonClickListener mOnEditButtonClickListener,  OnDeleteButtonClickListener mOnDeleteButtonClickListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnEditButtonClickListener = mOnEditButtonClickListener;
        this.mOnDeleteButtonClickListener = mOnDeleteButtonClickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftar_alamat, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        DaftarAlamat model = mList.get(position);

        mHolder = holder;

        holder.tvJenisAlamat.setText(model.getJenis_alamat());
        holder.tvAlamat.setText(model.getAlamat());
        holder.tvCatatan.setText(model.getCatatan());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEditButtonClickListener.onEditButtonClick(v, position);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteButtonClickListener.onDeleteButtonClick(v, position);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class HolderData extends RecyclerView.ViewHolder {

        private TextView tvJenisAlamat, tvAlamat, tvCatatan;
        private ImageView ivEdit, ivDelete;

        private View container;

        HolderData(View v) {
            super(v);

            container = v;

            tvJenisAlamat = v.findViewById(R.id.tv_jenis_alamat);
            tvAlamat = v.findViewById(R.id.tv_alamat);
            tvCatatan = v.findViewById(R.id.tv_catatan);
            ivEdit = v.findViewById(R.id.iv_edit);
            ivDelete = v.findViewById(R.id.iv_delete);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int posisition);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(View v, int posisition);
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(View v, int posisition);
    }
}
