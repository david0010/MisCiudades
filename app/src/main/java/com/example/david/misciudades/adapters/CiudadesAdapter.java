package com.example.david.misciudades.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.misciudades.R;
import com.example.david.misciudades.helpers.ImageHelper;
import com.example.david.misciudades.modelo.Ciudad;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class CiudadesAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Ciudad> ciudades;
    private GoogleApiClient gac;

    public CiudadesAdapter(ArrayList<Ciudad> ciudades, GoogleApiClient gac, Context context) {
        this.ciudades = ciudades;
        this.gac = gac;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderCiudad(LayoutInflater.from(context).inflate(R.layout.ciudad, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HolderCiudad h = (HolderCiudad) holder;
        Ciudad c = ciudades.get(position);

        /*
            Establecemos una imagen por defecto (en caso de que no pueda conseguirse mediante la
            Google Client Api). Posteriormente, buscamos una mediante la Google Client Api, que las
            irá actualizando en segundo plano
         */
        h.foto.setImageResource(R.drawable.foto_default);
        if (gac != null) {
            Places.GeoDataApi.getPlacePhotos(gac, c.getIdGoogle()).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                @Override
                public void onResult(PlacePhotoMetadataResult placePhotoMetadataResult) {
                    if (placePhotoMetadataResult.getStatus().isSuccess()) {
                        PlacePhotoMetadataBuffer photoMetadataBuffer = placePhotoMetadataResult.getPhotoMetadata();

                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                        photoMetadata.getPhoto(gac).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                            @Override
                            public void onResult(PlacePhotoResult placePhotoResult) {
                                Bitmap photo = placePhotoResult.getBitmap();
                                h.foto.setImageBitmap(ImageHelper.getRoundedCornerBitmap(photo, 20));
                            }
                        });
                        photoMetadataBuffer.release();
                    }
                }
            });
        }

        h.nombre.setText(c.getNombre());
        h.pais.setText(c.getPais());
    }

    @Override
    public int getItemCount() {
        return ciudades.size();
    }

    static class HolderCiudad extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nombre;
        TextView pais;

        public HolderCiudad(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            nombre = itemView.findViewById(R.id.nombre);
            pais = itemView.findViewById(R.id.pais);
        }
    }
}