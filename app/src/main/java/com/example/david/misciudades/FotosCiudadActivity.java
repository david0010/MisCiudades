package com.example.david.misciudades;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.david.misciudades.adapters.FotosAdapter;
import com.example.david.misciudades.modelo.Ciudad;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/*
    En esta Activity mostramos 10 fotos de la ciudad seleccionada
 */
public class FotosCiudadActivity extends GoogleApiClientActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            Comprobamos si el dispositivo tiene conexión. Si no la tiene, al no poder obtenerse
            fotos, mostramos una pantalla de "Sin conexión". En dicha pantalla, una vez recuperemos
            la conexión podremos recargar la actividad
         */
        if (!isOnline()) {
            setContentView(R.layout.offline);
            findViewById(R.id.offlineIcon).setOnClickListener(this);
        } else {
            setContentView(R.layout.activity_fotos_ciudad);

            Ciudad ciudad = (Ciudad) getIntent().getSerializableExtra("ciudad");
            setTitle(ciudad.getNombre());

            mRecyclerView = findViewById(R.id.fotos);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            final ArrayList<Bitmap> fotos = new ArrayList<>();
            mAdapter = new FotosAdapter(fotos, FotosCiudadActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            // Se obtienen las fotos
            if (gac != null) {
                Places.GeoDataApi.getPlacePhotos(gac, ciudad.getIdGoogle()).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(PlacePhotoMetadataResult placePhotoMetadataResult) {
                        if (placePhotoMetadataResult.getStatus().isSuccess()) {
                            PlacePhotoMetadataBuffer photoMetadataBuffer = placePhotoMetadataResult.getPhotoMetadata();

                            for (int i = 0; i < 10; i++) {
                                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                                photoMetadata.getPhoto(gac).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                    @Override
                                    public void onResult(PlacePhotoResult placePhotoResult) {
                                        fotos.add(placePhotoResult.getBitmap());
                                        mAdapter.notifyItemChanged(fotos.size() - 1);
                                    }
                                });
                            }
                            // Liberamos el buffer para evitar que quede memoria residual
                            photoMetadataBuffer.release();
                        }
                    }
                });
            }
        }
    }

    /*
        Recargamos la actividad
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.offlineIcon) {
            finish();
            startActivity(getIntent());
        }
    }

    /*
        Se comprueba si el dispositivo tiene conexión
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
