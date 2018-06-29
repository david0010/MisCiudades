package com.example.david.misciudades.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.david.misciudades.R;

import java.util.ArrayList;

public class FotosAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Bitmap> fotos;

    public FotosAdapter(ArrayList<Bitmap> fotos, Context context) {
        this.fotos = fotos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderFoto(LayoutInflater.from(context).inflate(R.layout.foto, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HolderFoto h = (HolderFoto) holder;
        h.foto.setImageBitmap(fotos.get(position));
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    static class HolderFoto extends RecyclerView.ViewHolder {
        ImageView foto;

        public HolderFoto(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.fotoExtra);
        }
    }
}