package com.dhenao.miestadio.system;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.dhenao.miestadio.R;

public class OpcionesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefencias_miestadio);
    }
}