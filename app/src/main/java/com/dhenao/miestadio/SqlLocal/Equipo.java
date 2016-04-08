package com.dhenao.miestadio.SqlLocal;

/**
 * Created by david.henao on 04/04/2016.
 */
public class Equipo {

    //private variables
    int _id;
    String _name;
    String _detalle;

    // Empty constructor
    public Equipo(){

    }
    // constructor
    public Equipo(int id, String name, String _detalle){
        this._id = id;
        this._name = name;
        this._detalle = _detalle;
    }

    // constructor
    public Equipo(String name, String _detalle){
        this._name = name;
        this._detalle = _detalle;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._detalle;
    }

    // setting phone number
    public void setDetalle(String detalle){
        this._detalle = detalle;
    }
}
