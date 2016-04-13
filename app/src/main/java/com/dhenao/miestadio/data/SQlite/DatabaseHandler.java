package com.dhenao.miestadio.data.SQlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbmiestadio";

    //tablas
    private static final String TABLA_EQUIPOS = "equipos";

    //columnas de tabla equipos
    private static final String KEY_ID = "id";
    private static final String KEY_NOMB = "nombre";
    private static final String KEY_DESCR = "descripcion";
    private static final String KEY_IMG = "imagen";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creando las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EQUIPOS_TABLE = "CREATE TABLE " + TABLA_EQUIPOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMB + " TEXT,"
                + KEY_DESCR + " TEXT," + KEY_IMG + " TEXT" + ")";
        db.execSQL(CREATE_EQUIPOS_TABLE);
    }

    // actualizando base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EQUIPOS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adicionando equipo
    public void addEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMB, equipo.getNombre());
        values.put(KEY_DESCR, equipo.getDescripcion());
        values.put(KEY_IMG, equipo.getImagen());
        // Inserting Row
        db.insert(TABLA_EQUIPOS, null, values);
        db.close(); // Closing database connection
    }

    // Tomando un equipo
    public EquipoFutbol getEquipo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLA_EQUIPOS, new String[] { KEY_ID,
                        KEY_NOMB, KEY_DESCR,KEY_IMG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        EquipoFutbol equipo = new EquipoFutbol(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // devuelve EquipoFutbol
        return equipo;
    }

    // Getting All EquipoFutbols
    public List<EquipoFutbol> getAllEquipos() {
        List<EquipoFutbol> EquipoFutbolList = new ArrayList<EquipoFutbol>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLA_EQUIPOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EquipoFutbol equipo = new EquipoFutbol();
                equipo.setID(Integer.parseInt(cursor.getString(0)));
                equipo.setNombre(cursor.getString(1));
                equipo.setDescripcion(cursor.getString(2));
                equipo.setImagen(cursor.getString(3));
                // Adding EquipoFutbol to list
                EquipoFutbolList.add(equipo);
            } while (cursor.moveToNext());
        }
        // return EquipoFutbol list
        return EquipoFutbolList;
    }

    // Updating single EquipoFutbol
    public int updateEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMB, equipo.getNombre());
        values.put(KEY_DESCR, equipo.getDescripcion());
        values.put(KEY_IMG, equipo.getImagen());

        // updating row
        return db.update(TABLA_EQUIPOS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(equipo.getID()) });
    }

    // Deleting single EquipoFutbol
    public void deleteEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_EQUIPOS, KEY_ID + " = ?",
                new String[]{String.valueOf(equipo.getID())});
        db.close();
    }


    // Deleting single EquipoFutbol
    public void deleteAllEquipo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_EQUIPOS);
        db.close();
    }


    // Getting EquipoFutbols Count
    public int getCuentaEquipos() {
        String countQuery = "SELECT  * FROM " + TABLA_EQUIPOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }




}
