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
import com.dhenao.miestadio.data.SQlite.DatabaseHandler;
import com.dhenao.miestadio.data.SQlite.EquipoFutbol;
import com.dhenao.miestadio.data.SQlite.tbConfiguracion;
import com.dhenao.miestadio.system.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    Date fechaActual;
    int dia, mes, año;
    public boolean ejecutaConsulta = false;
    public int trespt = 0;
    DatabaseHandler db;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // establecer la orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // esconder la barra de titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        /*obtener fecha y hora del celular*/
        cal = new GregorianCalendar();
        fechaActual = cal.getTime();
        dia = Integer.parseInt((String) android.text.format.DateFormat.format("dd", fechaActual));
        mes = Integer.parseInt((String) android.text.format.DateFormat.format("MM", fechaActual));
        año = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", fechaActual));

        /********************************/

        /*para la base de datos SQlite*/
        db = new DatabaseHandler(getApplicationContext());
        tbConfiguracion tbconfiguracion = db.getConfiguracion();
        if (tbconfiguracion.getUltimoIngresoAño()==null || tbconfiguracion.getUltimoIngresoAño()==1983 || tbconfiguracion.getUltimoIngresoDia()!= dia || tbconfiguracion.getUltimoIngresoMes()!= mes || tbconfiguracion.getUltimoIngresoAño()!= año){
            //variable que indica si ya actualizó la informacion del dia - si es true es por que hay que actualizar
            ejecutaConsulta = true;
        }



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
        protected String doInBackground(String... args) {
            ConsultaMySql consultaMsql = new ConsultaMySql();
            trespt = consultaMsql.consultar(0, getApplicationContext(),""); //horas de juego - pueden cambiar en el transcurso del dia
            if (ejecutaConsulta){
                trespt = consultaMsql.consultar(1, getApplicationContext(),""); //equipos, se consulta una vez al dia
            }
            trespt = consultaMsql.consultar(2, getApplicationContext(),"");
            trespt = consultaMsql.consultar(3, getApplicationContext(),"");
            return null;
        }

        protected void onPostExecute(String Resp) {
            runOnUiThread(new Runnable() {
                public void run() {
                    timer = new Timer();
                    timer.schedule(task, SPLASH_SCREEN_DELAY);

                    if (!Config.conexionSistema) Toast.makeText(getApplicationContext(), "No se encuentran los datos activos", Toast.LENGTH_SHORT).show();
                    if (!Config.servidorEncontrado) Toast.makeText(getApplicationContext(), "No ha encontrado el servidor, por favor conéctese correctamente" , Toast.LENGTH_SHORT).show();

                    if (ejecutaConsulta) {
                        db.updateConfiguracion(new tbConfiguracion(dia, mes, año));
                        db.updateEquipo(new EquipoFutbol(1, Config.pEquipo1NombreMaM, Config.pEquipo1DescripcionMaM, Config.pEquipo1ImagenMaM));
                        db.updateEquipo(new EquipoFutbol(2,Config.pEquipo2NombreMaM,Config.pEquipo2DescripcionMaM,Config.pEquipo2ImagenMaM));
                    }

                }
            });
        }
    }
}
