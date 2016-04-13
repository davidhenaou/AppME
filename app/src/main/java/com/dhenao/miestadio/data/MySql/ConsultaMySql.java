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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
            String tempConsulta = "";
            JSONObject c;

            switch (id) {
                case 0: tempConsulta = Config.URL_MYSQL_HORASYFECHASJUEGO; break;
                case 1: tempConsulta = Config.URL_MYSQL_EQUIPOS; break;
                case 2: tempConsulta = Config.URL_MYSQL_MINUTOAMINUTO; break;
            }


            try {
                JSONObject json = jParser.makeHttpRequest(tempConsulta, "GET", params);
                if (json != null) {
                    // chequeando estado
                    int estado = json.getInt("estado");
                    if (estado == 1) {

                        switch (id) {
                            case 0: //CONSULTO HORAS Y FECHA DE JUEGO
                                Log.d("horas juego: ", json.toString());
                                /*obtener fecha y hora del celular*/
                                Calendar cal = new GregorianCalendar();
                                Date fechamovil = cal.getTime();
                                /********************************/
                                // horarios encontrados
                                JSONArray minutosJuego = json.getJSONArray("horasprog");
                                c = minutosJuego.getJSONObject(0);
                                String ttextoConsulta = c.getString("horasjuego");
                                String tfechaPartido = recortarTexto(ttextoConsulta, "<table>\n<tbody>\n<tr>\n<td>", 10);
                                String tfechaHoraPartido = tfechaPartido + " " + recortarTexto(ttextoConsulta, "<td>Primer Tiempo</td>\n<td>", 5);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                Date consultafecha = fechamovil;
                                try {
                                    consultafecha = format.parse(tfechaHoraPartido);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Config.horapt = consultafecha;

                                tfechaHoraPartido = tfechaPartido + " " + recortarTexto(ttextoConsulta, "<td>Segundo Tiempo</td>\n<td>", 5);
                                consultafecha = fechamovil;
                                try {
                                    consultafecha = format.parse(tfechaHoraPartido);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Config.horast = consultafecha;

                                String textoRecortado = recortarTexto(ttextoConsulta, "<td>Adición PT</td>\n<td>", 1);
                                Config.repopt = Integer.parseInt(textoRecortado);

                                textoRecortado = recortarTexto(ttextoConsulta, "<td>Adición ST</td>\n<td>", 1);
                                Config.repost = Integer.parseInt(textoRecortado);
                                break;


                            case 1://consulta de equipos que estan jugando
                                Log.d("Los Equipos: ", json.toString());
                                // equipos encontrados
                                JSONArray equiposJson = json.getJSONArray("equipos");
                                c = equiposJson.getJSONObject(0);
                                Config.pEquipo1NombreMaM = c.getString("equipo");
                                Config.pEquipo1DescripcionMaM = c.getString("descripcion");
                                Config.pEquipo1ImagenMaM = c.getString("imagen");

                                c = equiposJson.getJSONObject(1);
                                Config.pEquipo2NombreMaM = c.getString("equipo");
                                Config.pEquipo2DescripcionMaM = c.getString("descripcion");
                                Config.pEquipo2ImagenMaM = c.getString("imagen");
                                break;

                            case 2:


                                break;
                        }
                        Respuest = 1; //encontro registros
                    } else {
                        // no hay equipos encontrados
                        Log.d("no encontro una fecha: ", json.toString());
                        Respuest = 0; //no encontro registros
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }else{
            Respuest = -1; //retorna no tiene datos activo
        }
        return Respuest;
    }

    public String recortarTexto(String textoCompleto, String textoarecortar, int caracteres){
        int textobuscado = textoCompleto.indexOf(textoarecortar);
        textoarecortar = textoCompleto.substring(textobuscado + textoarecortar.length(), textobuscado +  textoarecortar.length() + caracteres);
        return textoarecortar;
    }

    public static int restarFechas(Date fechaIn, Date fechaFinal ){
        long in = fechaIn.getTime();
        long fin = fechaFinal.getTime();
        Long diff= (fin-in)/1000;
        return diff.intValue();
    }


}