package com.dhenao.miestadio.data.SQlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhenao.miestadio.system.Config;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbmiestadio";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creando las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {        
        String ACCIONSQLITE;
        //creamos la tabla de configuracion        
        ACCIONSQLITE = "CREATE TABLE tbconfig ( ultingresodia INTEGER, ultingresomes INTEGER, ultingresoano INTEGER  )";   db.execSQL(ACCIONSQLITE);
        ACCIONSQLITE = "INSERT INTO tbconfig ( ultingresodia, ultingresomes, ultingresoano )values ( 26, 9, 1983 )";   db.execSQL(ACCIONSQLITE);
                
        //creamos la tabala de equipos
        ACCIONSQLITE = "CREATE TABLE tbequipos ( id INTEGER PRIMARY KEY, nombre TEXT, descripcion TEXT, imagen TEXT )"; db.execSQL(ACCIONSQLITE);
        ACCIONSQLITE = "INSERT INTO tbequipos ( id, nombre, descripcion, imagen )values ( 1, 'equipo1', '', '' )";   db.execSQL(ACCIONSQLITE);
        ACCIONSQLITE = "INSERT INTO tbequipos ( id, nombre, descripcion, imagen )values ( 2, 'equipo2', '', '' )";   db.execSQL(ACCIONSQLITE);
    }

    // actualizando base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbconfig");  onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS tbequipos");  onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    /*
     * crud de tbconfiguracion
     */

    public tbConfiguracion getConfiguracion() {
        tbConfiguracion configuracion = new tbConfiguracion();;
        String selectQuery = "SELECT  * FROM tbconfig";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                configuracion.setFechaPartido(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        // return EquipoFutbol list
        return configuracion;
    }

    public int updateConfiguracion(tbConfiguracion configuracion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ultingresodia", configuracion.getUltimoIngresoDia());
        values.put("ultingresomes", configuracion.getUltimoIngresoMes());
        values.put("ultingresoano", configuracion.getUltimoIngresoAño());
        // updating row
        return db.update("tbconfig", values, "-10 < ?",
                new String[] { String.valueOf(configuracion.getUltimoIngresoDia()) });
    }


    /*
     * crud de tbequipos
     */

    public int updateEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", equipo.getNombre());
        values.put("descripcion", equipo.getDescripcion());
        values.put("imagen", equipo.getImagen());

        return db.update("tbequipos", values, "id = ?",
                new String[] { String.valueOf(equipo.getID()) });
    }


    // Tomando un equipo
    public EquipoFutbol getEquipo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("tbequipos", new String[]{"id",
                        "nombre", "descripcion", "imagen"}, "id =?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        EquipoFutbol equipo = new EquipoFutbol(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // devuelve EquipoFutbol
        return equipo;
    }


    public void actualizarInfoEquipos(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        EquipoFutbol tbequipos = db.getEquipo(1);
        Config.pEquipo1NombreMaM = tbequipos.getNombre();
        Config.pEquipo1DescripcionMaM = tbequipos.getDescripcion();
        Config.pEquipo1ImagenMaM = tbequipos.getImagen();
        tbequipos = db.getEquipo(2);
        Config.pEquipo2NombreMaM = tbequipos.getNombre();
        Config.pEquipo2DescripcionMaM = tbequipos.getDescripcion();
        Config.pEquipo2ImagenMaM = tbequipos.getImagen();
    }














    // Adicionando equipo
    public void addEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", equipo.getNombre());
        values.put("descripcion", equipo.getDescripcion());
        values.put("imagen", equipo.getImagen());
        // Inserting Row
        db.insert("tbequipos", null, values);
        db.close(); // Closing database connection
    }


    // Getting All EquipoFutbols
    public List<EquipoFutbol> getAllEquipos() {
        List<EquipoFutbol> EquipoFutbolList = new ArrayList<EquipoFutbol>();
        // Select All Query
        String selectQuery = "SELECT  * FROM tbequipos";

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



    // Deleting single EquipoFutbol
    public void deleteEquipo(EquipoFutbol equipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbequipos", "id = ?",
                new String[]{String.valueOf(equipo.getID())});
        db.close();
    }


    // Deleting single EquipoFutbol
    public void deleteAllEquipo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tbequipos");
        db.close();
    }


    // Getting EquipoFutbols Count
    public int getCuentaEquipos() {
        String countQuery = "SELECT  * FROM tbequipos";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }




}
