package com.solvedev.haloskin.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.ListMenu;

import java.util.ArrayList;

public class OtherMenuAdapter extends ArrayAdapter<ListMenu> {

    Context context;
    int resouce;
    ArrayList<ListMenu> arrayMenu = new ArrayList<ListMenu>();

    public OtherMenuAdapter(@NonNull Context context, int resource, ArrayList<ListMenu> arrayMenu ) {
        super(context, resource, arrayMenu);

        this.context = context;
        this.resouce = resource;
        this.arrayMenu = arrayMenu;
    }


    class Holder{
        TextView tvJudul;
        ImageView ivFoto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Holder holder;

        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(resouce, parent, false);
            holder.tvJudul = convertView.findViewById(R.id.tv_judul);
            holder.ivFoto = convertView.findViewById(R.id.iv_foto);

        }else{
            holder = (Holder)convertView.getTag();
        }

        holder.tvJudul.setText(getItem(position).getJudul());
        holder.ivFoto.setImageDrawable(getItem(position).getFoto());


        return convertView;
    }
}
