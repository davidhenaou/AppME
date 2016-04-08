package com.dhenao.miestadio.pantallas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.Equipos;
import com.dhenao.miestadio.system.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MinutoaMinuto extends Activity {
    private TextView equipo1, equipo2;
    private List<Equipos> listaEquipos;
    private Equipos equipos;
    private int posicion=0;
    private boolean respt;
    private String mensajeresp;
    private ProgressDialog progressDialog;


    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minutoaminuto);
        equipo1 = (TextView) findViewById(R.id.nombreequipo1);
        equipo2 = (TextView) findViewById(R.id.nombreequipo2);
        //new Mostrar().execute();
    }


    public void clickMinutoAminuto1(View target) { //para cargar la ventana de minuto a minuto
        //progressDialog = ProgressDialog.show(getApplicationContext(), "Descargando información", "Por favor espere", true);
        Log.d("Inicio la ejecucion", "ejecucion segundo plano");
        new Mostrar().execute();
    }

    class Mostrar extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            //if(filtrarDatos())mostrarPersona(posicion);
            respt = filtrarDatos();
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Termino la ejecucion", "ejecucion segundo plano");
            //progressDialog.dismiss();
            //if((!respt)) showAlert("Se presento un error en la consulta. " + mensajeresp ,"Atención");
        }
    }


    private String mostrar(String miurl){
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(miurl);
        //HttpGet httppost = new HttpGet(Config.URL_MYSQL_EQUIPOS);
        String resultado="";
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado= convertStreamToString(instream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }


    private boolean filtrarDatos(){
        //listaEquipos.clear();
        String data=mostrar(Config.URL_MYSQL_EQUIPOS);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                if (json.get("estado")==1) {
                    JSONArray jsonArray = json.optJSONArray("equipos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        equipos = new Equipos();
                        JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                        equipos.setNombre(jsonArrayChild.optString("equipo"));

                        listaEquipos.add(equipos);
                    }
                    Log.d("Devolvio datos", "esta devolviendo datos");
                    return true;
                }else{
                    mensajeresp = json.get("mensaje").toString();
                    Log.d("Error buscando", mensajeresp);
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Error buscando", e.toString());
            }
        }
        Log.d("Error buscando", "esta vacio");
        return false;
    }


    private void mostrarPersona(final int posicion){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Equipos equipos = listaEquipos.get(posicion);
                equipo1.setText(equipos.getNombre());
            }
        });
    }


    private void showAlert(String message, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(titulo)
                .setCancelable(false)
                .setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //no hacer nada
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
