package com.dhenao.miestadio.pantallas;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.MySql.ConsultaMySql;
import com.dhenao.miestadio.system.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    //Duracion de la pantalla
    private static final long SPLASH_SCREEN_DELAY = 3000;
    Timer timer;
    TimerTask task;
    CargaImagenes nuevaTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // establecer la orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // esconder la barra de titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        /*obtener fecha y hora del celular*/
        //Calendar cal = new GregorianCalendar();
        //Config.horapt = cal.getTime();
        /********************************/


        task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(Splash.this, ActividadPrincipal.class);
                startActivity(mainIntent);
                finish();
            }
        };
        new TareaAsincConsultaInicial().execute();
    }


    class TareaAsincConsultaInicial extends AsyncTask<String,String,String> {
        public int trespt;
        protected String doInBackground(String... args) {
            ConsultaMySql consultaMsql = new ConsultaMySql();
            trespt = consultaMsql.consultar(0, getApplicationContext());
            trespt = consultaMsql.consultar(1, getApplicationContext());
            trespt = consultaMsql.consultar(2, getApplicationContext());
            trespt = consultaMsql.consultar(3, getApplicationContext());
            return null;
        }

        protected void onPostExecute(String Resp) {
            runOnUiThread(new Runnable() {
                public void run() {
                    timer = new Timer();
                    timer.schedule(task, SPLASH_SCREEN_DELAY);

                    nuevaTarea = new CargaImagenes();
                    nuevaTarea.execute(Config.pEquipo1ImagenMaM);

                    if (!Config.conexionSistema) Toast.makeText(getApplicationContext(), "No se encuentran los datos activos", Toast.LENGTH_SHORT).show();
                    if (!Config.servidorEncontrado) Toast.makeText(getApplicationContext(), "No ha encontrado el servidor, por favor conéctese correctamente" , Toast.LENGTH_SHORT).show();
                    /*switch (trespt) {
                        case -2: Toast.makeText(getApplicationContext(), "No hay conexión" , Toast.LENGTH_SHORT).show(); break;
                        //case -1: Toast.makeText(getApplicationContext(), "No tiene los datos activos" , Toast.LENGTH_SHORT).show(); break;
                    }*/
                }
            });
        }
    }



    private class CargaImagenes extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //String url = params[0];
            Bitmap imagen = descargarImagen(Config.pEquipo1ImagenMaM);
            Config.pEquipo1ImagenDrawable = new BitmapDrawable(Resources.getSystem(), imagen);
            imagen = descargarImagen(Config.pEquipo2ImagenMaM);
            Config.pEquipo2ImagenDrawable = new BitmapDrawable(Resources.getSystem(), imagen);

            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
        }

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
