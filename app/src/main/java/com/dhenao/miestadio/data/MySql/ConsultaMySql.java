package com.dhenao.miestadio.data.MySql;


import android.util.Log;

import com.dhenao.miestadio.system.Config;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultaMySql {


    public ConsultaMySql(int id) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.makeHttpRequest(Config.URL_MYSQL_EQUIPOS, "GET", params);
        Log.d("Los Equipos: ", json.toString());

        try {
            // chequeando estado
            int estado = json.getInt("estado");
            if (estado == 1) {
                // equipos encontrados
                JSONArray equiposJson = json.getJSONArray("equipos");
                JSONObject c;
                c = equiposJson.getJSONObject(0);
                Config.pEquipo1NombreMaM = c.getString("equipo");
                Config.pEquipo1DescripcionMaM = c.getString("descripcion");
                Config.pEquipo1ImagenMaM = c.getString("imagen");

                c = equiposJson.getJSONObject(1);
                Config.pEquipo2NombreMaM = c.getString("equipo");
                Config.pEquipo2DescripcionMaM = c.getString("descripcion");
                Config.pEquipo2ImagenMaM = c.getString("imagen");

            } else {
                // no hay equipos encontrados
                Log.d("no encontro equipos: ", json.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}