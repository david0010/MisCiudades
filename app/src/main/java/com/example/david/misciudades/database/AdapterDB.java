package com.example.david.misciudades.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdapterDB {
    protected final String DBName;

    protected Context mContext;
    protected static DatabaseHelper mDbHelper;
    protected SQLiteDatabase db;

    public AdapterDB(Context context, String DBName) {
        this.mContext = context;
        this.DBName = DBName;
    }

    public void openDb() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mContext, DBName);
        }
        db = mDbHelper.getWritableDatabase();
    }

    public void closeDb() {
        mDbHelper.close();
    }

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String DBName) {
            super(context, DBName, null, ConstantsDB.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ConstantsDB.CIUDADES_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("MisCiudades", "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS routes");
            onCreate(db);
        }
    }
}
