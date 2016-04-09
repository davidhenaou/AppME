package com.dhenao.miestadio.data.MySql;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.system.Config;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultaMySql {

    public ConsultaMySql() {
    }

    public int consultar(int id, Context context) {
        int Respuest = 0; /*-2 no hay conexion, no encuentra el servidor
                            -1 no tiene datos activo en la conexion
                            0 no hay registros encontrados
                            1 encontro registros
                          */
        Boolean conexion = false;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //verifico si hay conexion
        if (networkInfo != null && networkInfo.isConnected()) conexion = true;

        if (conexion) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONParser jParser = new JSONParser();

            if (id == 1) {
                try {
                    JSONObject json = jParser.makeHttpRequest(Config.URL_MYSQL_EQUIPOS, "GET", params);
                    if (json != null) {

                        // chequeando estado
                        int estado = json.getInt("estado");
                        if (estado == 1) {
                            Log.d("Los Equipos: ", json.toString());
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
                            Respuest = 1; //encontro registros
                        } else {
                            // no hay equipos encontrados
                            Log.d("no encontro equipos: ", json.toString());
                            Respuest = 0; //no encontro registros
                        }
                    }else{
                        // no alcanzo a hacer la consulta por conexion
                        Log.d("no hay conexion: ", "revisar la conexion");
                        Respuest = -2; //no hay conexion, no encuentra el servidor
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Respuest = -1; //retorna no tiene datos activo
        }
        return Respuest;
    }
}