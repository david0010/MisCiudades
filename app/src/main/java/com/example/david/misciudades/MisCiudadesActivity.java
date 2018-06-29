package com.example.david.misciudades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.david.misciudades.adapters.CiudadesAdapter;
import com.example.david.misciudades.database.CiudadDB;
import com.example.david.misciudades.database.ConstantsDB;
import com.example.david.misciudades.listeners.RecyclerItemClickListener;
import com.example.david.misciudades.modelo.Ciudad;

import java.util.ArrayList;

/*
    En esta actividad mostramos la lista de ciudades almacenadas. Por defecto, tenemos en base de
    datos Murcia, Londres, Berlín, Nueva York y Chicago.
    La foto de cada ciudad se recupera mediante la Places Api for Android de Google
 */
public class MisCiudadesActivity extends GoogleApiClientActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_ciudades);
        setTitle("Mis Ciudades");

        mRecyclerView = findViewById(R.id.ciudades);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CiudadDB dbCiudades = new CiudadDB(this, ConstantsDB.DB_NAME);
        final ArrayList<Ciudad> ciudades = dbCiudades.loadCiudades();
        for (Ciudad c : ciudades) {
            Log.d("Ciudad", c.toString());
        }

        mAdapter = new CiudadesAdapter(ciudades, gac, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    /*
                        Al pulsar sobre una ciudad, accedemos a FotosCiudadActivity, donde se mostrará
                        una lista de fotos de dicha ciudad
                    */
                    @Override
                    public void onItemClick(View view, int position) {
                        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                        Ciudad item = ciudades.get(itemPosition);
                        Intent is = new Intent(MisCiudadesActivity.this, FotosCiudadActivity.class);
                        is.putExtra("ciudad", item);
                        startActivity(is);
                    }
                })
        );
    }
}
