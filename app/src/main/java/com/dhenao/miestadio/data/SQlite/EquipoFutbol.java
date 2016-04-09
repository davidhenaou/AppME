package com.dhenao.miestadio.data.SQlite;

public class EquipoFutbol {

    //private variables
    int _id;
    String _nombre;
    String _descripcion;
    String _imagen;


    // Empty constructor
    public EquipoFutbol(){

    }
    // constructor
    public EquipoFutbol(int id, String nombre, String descripcion, String _imagen){
        this._id = id;
        this._nombre = nombre;
        this._descripcion = descripcion;
        this._imagen = _imagen;
    }

    // constructor
    public EquipoFutbol(String nombre, String descripcion, String imagen){
        this._nombre = nombre;
        this._descripcion = descripcion;
        this._imagen = imagen;
    }

    public int getID(){ return this._id; }

    public void setID(int id){ this._id = id; }

    public String getNombre(){ return this._nombre; }

    public void setNombre(String nombre){ this._nombre = nombre; }

    public String getDescripcion(){ return this._descripcion; }

    public void setDescripcion(String descripcion){ this._descripcion = descripcion; }

    public String getImagen(){ return this._imagen; }

    public void setImagen(String imagen){ this._imagen = imagen; }

}
