package com.example.david.misciudades;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.david.misciudades.database.CiudadDB;
import com.example.david.misciudades.database.ConstantsDB;
import com.example.david.misciudades.modelo.Ciudad;

import java.util.ArrayList;

public class DataBaseTest extends ApplicationTestCase<Application> {
    private CiudadDB ciudadDb;
    private Ciudad c1;
    private Ciudad c2;
    private Ciudad c3;
    private Ciudad c4;
    private Ciudad c5;

    public DataBaseTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ciudadDb = new CiudadDB(mContext, ConstantsDB.DB_NAME_TESTS);
        c1 = new Ciudad("Murcia", "España", 37.99223990000001, -1.1306544000000003, "ChIJf4yS1fiBYw0RmqvEOJsSJ9Y");
        c2 = new Ciudad("Londres", "Reino Unido", 51.5073509, -0.1277583, "ChIJdd4hrwug2EcRmSrV3Vo6llI");
        c3 = new Ciudad("Berlín", "Alemania", 52.520006599999995, -13.404954, "ChIJAVkDPzdOqEcRcDteW0YgIQQ");
        c4 = new Ciudad("Nueva York", "EE. UU.", 43.2994285, -74.21793260000001, "ChIJqaUj8fBLzEwRZ5UY3sHGz90");
        c5 = new Ciudad("Chicago", "EE. UU.", 41.8781136, -87.6297982, "ChIJ7cv00DwsDogRAMDACa2m4K8");
    }

    public void testCiudadBD(){
        // INSERT
        ciudadDb.insertCiudad(c1);
        ciudadDb.insertCiudad(c2);
        ciudadDb.insertCiudad(c3);

        // LOAD
        ArrayList<Ciudad> ciudades = ciudadDb.loadCiudades();
        assertEquals(3, ciudades.size());
        for (Ciudad ciudad : ciudades){
            Log.d("Ciudad", ciudad.toString());
        }

        // GET NUMERO CIUDADES
        long numeroCiudades = ciudadDb.getNumeroCiudades();
        assertEquals(3, numeroCiudades);

        // DELETE
        for (int i=0; i<ciudades.size(); i++) {
            ciudadDb.deleteCiudad(ciudades.get(i));
        }
        assertEquals(0, ciudadDb.getNumeroCiudades());
    }

    @Override
    public void tearDown() throws Exception {
        mContext.deleteDatabase(ConstantsDB.DB_NAME_TESTS);

        super.tearDown();
    }
}
