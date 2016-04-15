package com.dhenao.miestadio.data;

import com.dhenao.miestadio.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class DatosMinutoAMinuto {

    private String minutoi;
    private String titulo;
    private String accion;
    private String minutod;

    public DatosMinutoAMinuto(String minutoi, String titulo, String accion,String minutod) {
        this.minutoi = minutoi;
        this.titulo = titulo;
        this.accion = accion;
        this.minutod = minutod;
    }

    /*
    public static final List<DatosMinutoAMinuto> MinutoItems = new ArrayList<DatosMinutoAMinuto>();

    static {
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James","gol de prueba","60"));
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James","gol de prueba","44"));
        MinutoItems.add(new DatosMinutoAMinuto("36","Gol-Antonio Greizmann","gol de prueba",""));
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James ","gol de prueba","32"));
        MinutoItems.add(new DatosMinutoAMinuto("15","Gol-Antonio Greizmann","gol de prueba",""));
        MinutoItems.add(new DatosMinutoAMinuto("1","Gol-Antonio Greizmann","gol de prueba",""));
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James","gol de prueba","60"));
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James","gol de prueba","44"));
        MinutoItems.add(new DatosMinutoAMinuto("36","Gol-Antonio Greizmann","gol de prueba",""));
        MinutoItems.add(new DatosMinutoAMinuto("","Gol-James ","gol de prueba","32"));
        MinutoItems.add(new DatosMinutoAMinuto("15","Gol-Antonio Greizmann","gol de prueba",""));
        MinutoItems.add(new DatosMinutoAMinuto("1","Gol-Antonio Greizmann","gol de prueba",""));
    }*/

    public String getMinutoi() {
        return minutoi;
    }

    public String getMinutod() {
        return minutod;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAccion() {
        return accion;
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
