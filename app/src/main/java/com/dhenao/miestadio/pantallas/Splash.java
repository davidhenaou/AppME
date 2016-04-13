package com.dhenao.miestadio.pantallas;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.MySql.ConsultaMySql;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    //Duracion de la pantalla
    private static final long SPLASH_SCREEN_DELAY = 3000;
    Timer timer;
    TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // establecer la orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // esconder la barra de titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

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
            return null;
        }

        protected void onPostExecute(String Resp) {
            runOnUiThread(new Runnable() {
                public void run() {
                    timer = new Timer();
                    timer.schedule(task, SPLASH_SCREEN_DELAY);

                    switch (trespt) {
                        case -2: Toast.makeText(getApplicationContext(), "No hay conexión" , Toast.LENGTH_SHORT).show(); break;
                        case -1: Toast.makeText(getApplicationContext(), "No tiene los datos activos" , Toast.LENGTH_SHORT).show(); break;
                    }
                }
            });
        }
    }
}
