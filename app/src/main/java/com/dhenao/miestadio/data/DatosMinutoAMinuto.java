package com.dhenao.miestadio.data;

import com.dhenao.miestadio.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class DatosMinutoAMinuto {

    private String descripcion;
    private String nombre;
    private int idDrawable;

    public DatosMinutoAMinuto(String descripcion, String nombre, int idDrawable) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.idDrawable = idDrawable;
    }

    public static final List<DatosMinutoAMinuto> FOTOS = new ArrayList<DatosMinutoAMinuto>();
    public static final List<DatosMinutoAMinuto> VIDEOS = new ArrayList<DatosMinutoAMinuto>();
    static {
        FOTOS.add(new DatosMinutoAMinuto("foto 1", "Jugada 1", R.drawable.foto1));
        FOTOS.add(new DatosMinutoAMinuto("foto 2", "Jugada 2", R.drawable.foto2));
        FOTOS.add(new DatosMinutoAMinuto("foto 3", "Jugada 3", R.drawable.foto3));
        FOTOS.add(new DatosMinutoAMinuto("foto 4", "Jugada 4", R.drawable.foto4));
        FOTOS.add(new DatosMinutoAMinuto("foto 5", "Jugada 5", R.drawable.foto5));
        FOTOS.add(new DatosMinutoAMinuto("foto 6", "Jugada 6", R.drawable.foto6));
    }

    public String getMultimedia() {
        return descripcion;
    }

    public String getMultimedia1nombre() {
        return nombre;
    }

    public int getMultimedia1IdDrawable() {
        return idDrawable;
    }

    public static ArrayList<DatosMinutoAMinuto> randomList(int count) {
        Random random = new Random();
        HashSet<DatosMinutoAMinuto> items = new HashSet<DatosMinutoAMinuto>();

        // Restricción de tamaño
        count = Math.min(count, 10 ); //FOTOS.length);

        while (items.size() < count) {
           // items.add(LISTAS[random.nextInt(LISTAS.length)]);
        }

        return new ArrayList<DatosMinutoAMinuto>(items);
    }
}
