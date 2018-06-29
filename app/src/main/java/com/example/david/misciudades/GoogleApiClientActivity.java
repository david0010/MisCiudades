package com.example.david.misciudades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public abstract class GoogleApiClientActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    protected GoogleApiClient gac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gac = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        // Conectamos con Google Api Client
        super.onStart();
        if (gac != null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        // Desconectamos Google Api Client
        if (gac != null && gac.isConnected()) {
            gac.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Aqui manejamos el evento onConnected de la Google Api Client
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Aqui manejamos el evento onConnectionSuspended de la Google Api Client
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Aqui manejamos el evento onConnectionFailed de la Google Api Client
    }

}
