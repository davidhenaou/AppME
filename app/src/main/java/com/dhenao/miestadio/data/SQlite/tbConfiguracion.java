package com.dhenao.miestadio.data.SQlite;


public class tbConfiguracion {

    Integer ultimoIngresoDia, ultimoIngresoMes, ultimoIngresoAño;

    public tbConfiguracion(){
    }

    public tbConfiguracion(Integer ultimoIngresodia, Integer ultimoIngresomes, Integer ultimoIngresoAño){
        this.ultimoIngresoDia = ultimoIngresodia;
        this.ultimoIngresoMes = ultimoIngresomes;
        this.ultimoIngresoAño = ultimoIngresoAño;
    }

    public Integer getUltimoIngresoDia(){
        return this.ultimoIngresoDia;
    }

    public Integer getUltimoIngresoMes(){
        return this.ultimoIngresoMes;
    }

    public Integer getUltimoIngresoAño(){
        return this.ultimoIngresoAño;
    }

    public void setFechaPartido(Integer ultimoIngresodia, Integer ultimoIngresomes, Integer ultimoIngresoAño){
        this.ultimoIngresoDia = ultimoIngresodia;
        this.ultimoIngresoMes = ultimoIngresomes;
        this.ultimoIngresoAño = ultimoIngresoAño;
    }


}
