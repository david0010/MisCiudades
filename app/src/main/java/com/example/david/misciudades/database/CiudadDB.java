package com.example.david.misciudades.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.example.david.misciudades.modelo.Ciudad;

import java.util.ArrayList;

public class CiudadDB extends AdapterDB {

    public CiudadDB(Context context, String DBName) {
        super(context, DBName);
    }

    private ContentValues tomaMapperContentValues(Ciudad ciudad) {
        ContentValues cv = new ContentValues();
        cv.put(ConstantsDB.NOMBRE, ciudad.getNombre());
        cv.put(ConstantsDB.PAIS, ciudad.getPais());
        cv.put(ConstantsDB.LATITUD, ciudad.getLatitud());
        cv.put(ConstantsDB.LONGITUD, ciudad.getLongitud());
        cv.put(ConstantsDB.ID_GOOGLE, ciudad.getIdGoogle());

        return cv;
    }

    public long insertCiudad(Ciudad ciudad) {
        this.openDb();
        long rowID = db.insert(ConstantsDB.CIUDADES, null, tomaMapperContentValues(ciudad));
        this.closeDb();

        return rowID;
    }

    public boolean existeCiudad(Ciudad ciudad) {
        this.openDb();
        String where = ConstantsDB.NOMBRE + " = ? AND " + ConstantsDB.PAIS + " = ? AND " + ConstantsDB.ID_GOOGLE + " = ?";
        String[] whereArgs = {ciudad.getNombre(), ciudad.getPais(), ciudad.getIdGoogle()};
        long numResultados = DatabaseUtils.queryNumEntries(db, ConstantsDB.CIUDADES, where, whereArgs);

        this.closeDb();

        return (numResultados > 0);
    }

    public ArrayList loadCiudades() {
        ArrayList list = new ArrayList<>();

        this.openDb();
        Cursor c = db.query(ConstantsDB.CIUDADES, null, null, null, null, null, null);

        try {
            while (c.moveToNext()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setId(c.getInt(0));
                ciudad.setNombre(c.getString(1));
                ciudad.setPais(c.getString(2));
                ciudad.setLatitud(c.getDouble(3));
                ciudad.setLongitud(c.getDouble(4));
                ciudad.setIdGoogle(c.getString(5));
                list.add(ciudad);
            }
        } finally {
            c.close();
        }
        this.closeDb();

        return list;
    }

    public long getNumeroCiudades() {
        this.openDb();
        long numRecetas = DatabaseUtils.queryNumEntries(db, ConstantsDB.CIUDADES);
        this.closeDb();

        return numRecetas;
    }

    /* Tendríamos la opción de eliminar una ciudad añadida. Habría que implementar la eliminación en
       la pantalla Mis Ciudades, por ejemplo capturando el deslizamiento de la Card correspondiente
     */
    public void deleteCiudad(Ciudad ciudad) {
        this.openDb();
        String where = ConstantsDB.ID + "= ?";
        String[] whereArgs = new String[]{String.valueOf(ciudad.getId())};
        db.delete(ConstantsDB.CIUDADES, where, whereArgs);
        this.closeDb();
    }
}
