package com.dhenao.miestadio.data.MySql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.system.Config;

import java.io.File;

public class RefrescarObjetos {

    public RefrescarObjetos(){
    }


    public void CargaPerfil(Activity context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Config.UsuarioPerfil = pref.getString("UsuarioPref", "").trim();
        Config.CorreoPerfil = pref.getString("CorreoPref", "").trim();
        Config.CelularPerfil = pref.getString("CelularPref", "").trim();
        String rutaImagenperf = pref.getString("ImagenPerf", "").trim();

        TextView edtUsuarioPerfil = (TextView) context.findViewById(R.id.perfil_usuario);
        TextView edtCorreoPerfil = (TextView) context.findViewById(R.id.perfil_correo);
        TextView edtCelularPerfil = (TextView) context.findViewById(R.id.perfil_celular);
        ImageView imagenPerfil = (ImageView) context.findViewById(R.id.icono_miperfil);

        edtUsuarioPerfil.setText(Config.UsuarioPerfil);
        edtCorreoPerfil.setText(Config.CorreoPerfil);
        edtCelularPerfil.setText(Config.CelularPerfil);

        if (!TextUtils.isEmpty(rutaImagenperf)){
            File imgFile = new File(rutaImagenperf);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imagenPerfil.setImageBitmap(myBitmap);
        }

    }


    public void RefrescarMinutoaMinuto(Activity context) {

        TextView txtequipo1 = (TextView) context.findViewById(R.id.nombreequipo1);
        TextView txtequipo2 = (TextView) context.findViewById(R.id.nombreequipo2);
        txtequipo1.setText(Config.pEquipo1NombreMaM);
        txtequipo2.setText(Config.pEquipo2NombreMaM);

    }


}
