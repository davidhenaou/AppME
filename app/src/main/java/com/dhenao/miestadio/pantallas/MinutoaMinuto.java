package com.dhenao.miestadio.pantallas;
import com.dhenao.miestadio.system.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;


import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.MySql.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinutoaMinuto extends Activity {
    private TextView txtequipo1, txtequipo2;
    private String equipoS1, equipoS2;
    private ListView listEquipos;
    private int posicion=0;
    private boolean respt;
    private String mensajeresp;
    private ProgressDialog pDialog;
    HashMap<String, String> mapEquipos;


    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> listaEquipos;
    JSONArray equipos = null;



    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minutoaminuto);
        txtequipo1 = (TextView) findViewById(R.id.nombreequipo1);
        txtequipo2 = (TextView) findViewById(R.id.nombreequipo2);
        listEquipos = (ListView) findViewById(R.id.listaEquipos);

        new MostrarEquipos().execute();
    }


    class MostrarEquipos extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MinutoaMinuto.this);
            pDialog.setMessage("Cargando Información, por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(Config.URL_MYSQL_EQUIPOS, "GET", params);
            Log.d("Los Equipos: ", json.toString());

            try {
                // chequeando estado
                int estado = json.getInt("estado");
                if (estado == 1) {
                    // equipos encontrados
                    equipos = json.getJSONArray("equipos");

                    // looping through All Products
                    for (int i = 0; i < equipos.length(); i++) {
                        JSONObject c = equipos.getJSONObject(i);

                        // Storing each json item in variable
                        String equipo = c.getString("equipo");
                        String descripcion = c.getString("descripcion");
                        String imagen = c.getString("imagen");

                        if (i==0) {
                            equipoS1 = equipo;
                        }else{
                            equipoS2 = equipo;
                        }

                        // creating new HashMap
                        mapEquipos = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        mapEquipos.put("id", String.valueOf(i));
                        mapEquipos.put("equipo", equipo);
                        mapEquipos.put("descripcion", descripcion);
                        mapEquipos.put("imagen", imagen);
                        //listaEquipos.add(map);
                    }
                } else {
                    // no equipos encontrados
                    Log.d("no encontro equipos: ", json.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    txtequipo1.setText(equipoS1);
                    txtequipo2.setText(equipoS2);
                    /*
                    ListAdapter adapter = new SimpleAdapter(
                            MinutoaMinuto.this, listaEquipos,
                            R.layout.minutoaminuto, new String[] { "nombre",
                            "descripcion"},
                            new int[] { R.id.nombre, R.id.descripcion });*/
                    // updating listview
                    //setListAdapter(adapter);
                }
            });

        }
    }


}
