package com.dhenao.miestadio.system;

import android.app.Activity;
import android.os.Bundle;

public class ActividadConfiguracion extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new OpcionesConfiguracion()).commit();
    }
}