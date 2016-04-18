package com.dhenao.miestadio.pantallas.Inflates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dhenao.miestadio.R;
//import com.dhenao.miestadio.data.DatosMultimedia;

/**
 * Llenado de las pestañas
 */
public class InflateLayoutNarracion extends Fragment {

    private LinearLayout LayautDescarga;

    public static InflateLayoutNarracion nuevaInstancia(int itemmenu,int indiceSeccion) {
        InflateLayoutNarracion fragment = new InflateLayoutNarracion();
        Bundle args = new Bundle();
        args.putInt("ITEMMENU", itemmenu);
        args.putInt("INDICE", indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenido_narracion, container, false);

        LayautDescarga = (LinearLayout) view.findViewById(R.id.contenidonarracion);
        //layoutManager = new GridLayoutManager(getActivity(), 1); //crear layout parametro son las columnas
        //LayautDescarga.setLayoutManager(layoutManager);

        int itemmenu = getArguments().getInt("ITEMMENU");
        int indiceSeccion = getArguments().getInt("INDICE");

        return view;

    }

}
