package com.dhenao.miestadio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
//import com.dhenao.miestadio.data.DatosMultimedia;

/**
 * Llenado de las pestañas
 */
public class ClaseTabsLLenadaConLayout extends Fragment {

    private LinearLayout LayautDescarga;
    private GridLayoutManager layoutManager;
    private ListAdapterMultimedia adaptadorMultimedia;

    public static ClaseTabsLLenadaConLayout nuevaInstancia(int itemmenu,int indiceSeccion) {
        ClaseTabsLLenadaConLayout fragment = new ClaseTabsLLenadaConLayout();
        Bundle args = new Bundle();
        args.putInt("ITEMMENU", itemmenu);
        args.putInt("INDICE", indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenido_item1, container, false);

        LayautDescarga = (LinearLayout) view.findViewById(R.id.contenidoitem1);
        //layoutManager = new GridLayoutManager(getActivity(), 1); //crear layout parametro son las columnas
        //LayautDescarga.setLayoutManager(layoutManager);

        int itemmenu = getArguments().getInt("ITEMMENU");
        int indiceSeccion = getArguments().getInt("INDICE");

        return view;

    }

}
