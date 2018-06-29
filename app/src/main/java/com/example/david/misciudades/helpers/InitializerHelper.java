package com.example.david.misciudades.helpers;

import android.content.Context;

import com.example.david.misciudades.constants.Constants;
import com.example.david.misciudades.database.CiudadDB;
import com.example.david.misciudades.database.ConstantsDB;
import com.example.david.misciudades.modelo.Ciudad;

import java.util.ArrayList;
import java.util.List;

public class InitializerHelper {

    public static void inicializarAppSiNecesario(Context context) {
        boolean appInicializada = SharedPreferencesHelper.getSharedPreference(context, Constants.APP_INICIALIZADA);
        if (!appInicializada) {
            inicializarCiudades(context);
        }
        SharedPreferencesHelper.updateSharedPreference(context, Constants.APP_INICIALIZADA, true);
    }

    private static void inicializarCiudades(Context context) {
        CiudadDB dbRec = new CiudadDB(context, ConstantsDB.DB_NAME);
        if (dbRec.getNumeroCiudades() == 0) {
            List<Ciudad> ciudades = getCiudadesIniciales();
            for (Ciudad c : ciudades) {
                dbRec.insertCiudad(c);
            }
        }
    }

    private static ArrayList getCiudadesIniciales() {
        ArrayList<Ciudad> ciudades = new ArrayList<>();
        Ciudad murcia = new Ciudad("Murcia", "España", 37.99223990000001, -1.1306544000000003, "ChIJf4yS1fiBYw0RmqvEOJsSJ9Y");
        Ciudad londres = new Ciudad("Londres", "Reino Unido", 51.5073509, -0.1277583, "ChIJdd4hrwug2EcRmSrV3Vo6llI");
        Ciudad berlin = new Ciudad("Berlín", "Alemania", 52.520006599999995, 13.404954, "ChIJAVkDPzdOqEcRcDteW0YgIQQ");
        Ciudad nuevaYork = new Ciudad("Nueva York", "EE. UU.", 43.2994285, -74.21793260000001, "ChIJqaUj8fBLzEwRZ5UY3sHGz90");
        Ciudad chicago = new Ciudad("Chicago", "EE. UU.", 41.8781136, -87.6297982, "ChIJ7cv00DwsDogRAMDACa2m4K8");
        ciudades.add(murcia);
        ciudades.add(londres);
        ciudades.add(berlin);
        ciudades.add(nuevaYork);
        ciudades.add(chicago);

        return ciudades;
    }
}
