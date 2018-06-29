package com.example.david.misciudades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.david.misciudades.database.CiudadDB;
import com.example.david.misciudades.database.ConstantsDB;
import com.example.david.misciudades.modelo.Ciudad;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.misCiudades).setOnClickListener(this);
        findViewById(R.id.buscarCiudades).setOnClickListener(this);
        findViewById(R.id.sobreNosotros).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.misCiudades:
                Log.i("Click", "Mis Ciudades");
                Intent is = new Intent(MainActivity.this, MisCiudadesActivity.class);
                MainActivity.this.startActivity(is);
                break;
            case R.id.buscarCiudades:
                iniciarBuscarCiudad();

                break;
            case R.id.sobreNosotros:
                Log.i("Click", "Sobre nosotros");
                Intent ic = new Intent(MainActivity.this, SobreNosotrosActivity.class);
                startActivity(ic);
                break;
        }
    }

    /* Al ser una función muy sencilla, simplemente ciudades y añadir, para no incurrir en más navegación,
       se muestra el buscador de lugares de la Places Google API for Android sobre la pantalla principal.
       (MODE_OVERLAY)
     */
    public void iniciarBuscarCiudad() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.i("iniciarBuscarCiudad", e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.i("iniciarBuscarCiudad", e.getMessage());
        }
    }

    /*
        Obtenemos el lugar indicado por el usuario y construimos el objeto Ciudad con los campos:
        nombre, pais, latitud, longitud e idGoogle (éste último necesario para recuperar fotos
        posteriormente)
        Utilizamos la Places Google Api for Android (concretamente, PlaceAutocomplete)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addCiudad(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("PlaceAutocomplete", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addCiudad(Place place) {
        String nombre = (String) place.getName();
        String[] componentesDireccion = ((String) place.getAddress()).split(",");
        String pais = componentesDireccion[componentesDireccion.length - 1].trim();
        Double latitud = place.getLatLng().latitude;
        Double longitud = place.getLatLng().longitude;
        String idGoogle = place.getId();

        Ciudad ciudad = new Ciudad(nombre, pais, latitud, longitud, idGoogle);
        CiudadDB dbCiudades = new CiudadDB(this, ConstantsDB.DB_NAME);
        // Si la ciudad no fue añadida con anterioridad, se incorpora a la base de datos
        if (dbCiudades.existeCiudad(ciudad)) {
            Toast.makeText(this, "'" + place.getName() + "' ya fue añadida", Toast.LENGTH_SHORT).show();
        } else {
            dbCiudades.insertCiudad(ciudad);
            Toast.makeText(this, "Nueva ciudad añadida: '" + place.getName() + "'", Toast.LENGTH_SHORT).show();
        }
    }
}
