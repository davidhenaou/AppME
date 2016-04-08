package com.dhenao.miestadio.pantallas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.Equipos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class MinutoaMin extends Activity {
    private TextView equipo1, equipo2;
    private List<Equipos> listaEquipos;
    private Equipos equipos;
    private int posicion = 0;
    String insertUrl = "http://losurreas.eshost.com.ar/app/obtener_equipos1.php";
    String showUrl = "http://losurreas.eshost.com.ar/app/obtener_equipos1.php";
    TextView result;
    RequestQueue requestQueue;


    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minutoaminuto);
        equipo1 = (TextView) findViewById(R.id.nombreequipo1);
        equipo2 = (TextView) findViewById(R.id.nombreequipo2);
        //result = (TextView) findViewById(R.id.textocontenido);

        Mostrar(showUrl);

    }


    private String Mostrar(String myurl) {

        System.out.println("ww");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray equipos = response.getJSONArray("equipos");
                    for (int i = 0; i < equipos.length(); i++) {
                        JSONObject equipo = equipos.getJSONObject(i);

                        String firstname = equipo.getString("nombre");
                        String lastname = equipo.getString("detalle");

                        result.append(firstname + " " + lastname + " \n");
                    }
                    result.append("===\n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        requestQueue.add(jsonObjectRequest);

        return myurl;
    }




}