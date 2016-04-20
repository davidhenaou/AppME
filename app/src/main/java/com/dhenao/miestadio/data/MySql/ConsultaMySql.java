package com.dhenao.miestadio.data.MySql;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.DatosMinutoAMinuto;
import com.dhenao.miestadio.system.Config;
import com.dhenao.miestadio.system.Herramientas;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

        Herramientas herramientas = new Herramientas();
        boolean conexion = herramientas.verificaConexion(context);

        int Respuest = 0; /*-2 no hay conexion, no encuentra el servidor
                            -1 no tiene datos activo en la conexion
                            0 no hay registros encontrados
                            1 encontro registros
                          */

        if (conexion) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONParser jParser = new JSONParser();
            String tempConsulta = "";
            JSONObject c;

            switch (id) {
                case 0: tempConsulta = Config.URL_MYSQL_HORASYFECHASJUEGO; break;
                case 1: tempConsulta = Config.URL_MYSQL_EQUIPOS; break;
                case 2: tempConsulta = Config.URL_MYSQL_MINUTOAMINUTO; break;
                case 3: tempConsulta = Config.URL_MARCADOR_PARTIDO; break;
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
                                String tfechaPartido = herramientas.recortarTexto(ttextoConsulta, "FECHA(", 10);
                                String tfechaHoraPartido = tfechaPartido + " " + herramientas.recortarTexto(ttextoConsulta, "INICIA_PT(", 5);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                Date consultafecha = fechamovil;
                                try {
                                    consultafecha = format.parse(tfechaHoraPartido);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Config.horapt = consultafecha;

                                tfechaHoraPartido = tfechaPartido + " " + herramientas.recortarTexto(ttextoConsulta, "INICIA_ST(", 5);
                                consultafecha = fechamovil;
                                try {
                                    consultafecha = format.parse(tfechaHoraPartido);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Config.horast = consultafecha;

                                String textoRecortado = herramientas.recortarTexto(ttextoConsulta, "Adición_PT_(", 1);
                                Config.repopt = Integer.parseInt(textoRecortado);

                                textoRecortado = herramientas.recortarTexto(ttextoConsulta, "Adición_ST_(", 1);
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

                            case 2://consulta la informacion minuto a minuto
                                //Config.MinutoItems.clear();
                                Config.MinutoItems = new ArrayList<DatosMinutoAMinuto>();
                                //Config.MinutoItems.clear();
                                Log.d("Minuto a minuto: ", json.toString());
                                // registros de minuto a minuto
                                JSONArray minutoaminutolist = json.getJSONArray("minamin");

                                for (int i = 0; i < minutoaminutolist.length(); i++) {
                                    c = minutoaminutolist.getJSONObject(i);
                                    if (c.getInt("equipo")==0) Config.MinutoItems.add(new DatosMinutoAMinuto("",c.getString("descripcion"),c.getString("accion"),""));
                                    if (c.getInt("equipo")==1) Config.MinutoItems.add(new DatosMinutoAMinuto(c.getString("minuto"),c.getString("descripcion"),c.getString("accion"),""));
                                    if (c.getInt("equipo")==2) Config.MinutoItems.add(new DatosMinutoAMinuto("",c.getString("descripcion"),c.getString("accion"),c.getString("minuto")));
                                }

                                break;

                            case 3://consulto Marcadores del partido
                                Log.d("Marcadores: ", json.toString());
                                // equipos encontrados
                                JSONArray marcadoresJson = json.getJSONArray("marcador");
                                c = marcadoresJson.getJSONObject(0);
                                Config.marcadorEquipo1 = c.getString("golsequipo1");
                                Config.marcadorEquipo2 = c.getString("golsequipo2");
                                break;

                        }
                        Respuest = 1; //encontro registros
                    } else {
                        //consulta exitosa, no hay registros
                        Log.d("no encontro registros:", json.toString());
                        Respuest = 0; //no encontro registros
                    }
                    Config.servidorEncontrado = true;
                } else {
                    //no encuentra el servidor, hay conexion
                    Log.d("No hay servidor:", "json = null");
                    Config.servidorEncontrado = false;
                    Respuest = -2; //no hay servidor
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }else{
            //No tiene los datos activos
            Respuest = -1; //retorna no tiene datos activo
        }
        return Respuest;
    }

    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return imagen;
    }




}