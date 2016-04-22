package com.dhenao.miestadio.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.util.Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class Herramientas {

    public Herramientas(){
    }

    public boolean verificaConexion(Context context){
        //Verifico si esta el servicio de conexion encendido
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())Config.conexionSistema = true;
        return Config.conexionSistema;
    }

    public static int restarFechas(Date fechaIn, Date fechaFinal ){
        if (fechaIn==null)fechaIn = fechaFinal;
        if (fechaFinal==null)fechaFinal = fechaIn;

        long in = fechaIn.getTime();
        long fin = fechaFinal.getTime();
        Long diff= (fin-in)/1000;
        return diff.intValue();
    }

    public String recortarTexto(String textoCompleto, String textoarecortar, int caracteres){
        int textobuscado = textoCompleto.indexOf(textoarecortar);
        textoarecortar = textoCompleto.substring(textobuscado + textoarecortar.length(), textobuscado +  textoarecortar.length() + caracteres);
        return textoarecortar;
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
            //Config.pEquipo1ImagenDrawable = new BitmapDrawable(Resources.getSystem(), imagen);
            //imagen = descargarImagen(Config.pEquipo2ImagenMaM);
            //Config.pEquipo2ImagenDrawable = new BitmapDrawable(Resources.getSystem(), imagen);

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
