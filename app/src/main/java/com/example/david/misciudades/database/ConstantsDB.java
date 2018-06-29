package com.example.david.misciudades.database;

public class ConstantsDB {
    // Base de datos
    public static final String DB_NAME = "misciudades.db";
    public static final String DB_NAME_TESTS = "tests.db";
    public static final int DB_VERSION = 1;

    // Tabla Ciudades
    public static final String CIUDADES = "ciudades";
    public static final String ID = "_id";
    public static final String NOMBRE = "nombre";
    public static final String PAIS = "pais";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String ID_GOOGLE = "id_google";

    public static final String CIUDADES_SQL =
            "CREATE TABLE IF NOT EXISTS " + CIUDADES + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOMBRE + " TEXT NOT NULL, " +
                    PAIS + " TEXT NOT NULL, " +
                    LATITUD + "  DOUBLE NOT NULL, " +
                    LONGITUD + " DOUBLE NOT NULL, " +
                    ID_GOOGLE + " TEXT NOT NULL)";
}