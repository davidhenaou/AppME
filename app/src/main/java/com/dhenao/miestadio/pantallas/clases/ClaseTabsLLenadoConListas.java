package com.dhenao.miestadio.pantallas.clases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.DatosMultimedia;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
//import com.dhenao.miestadio.data.DatosMultimedia;

/**
 * Llenado de las pestañas
 */
public class ClaseTabsLLenadoConListas extends Fragment {

    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private ListAdapterMultimedia adaptadorMultimedia;
    //public static final List<DatosMultimedia> FOTOS = new ArrayList<DatosMultimedia>();

    public static ClaseTabsLLenadoConListas nuevaInstancia(int itemmenu,int indiceSeccion) {
        ClaseTabsLLenadoConListas fragment = new ClaseTabsLLenadoConListas();
        Bundle args = new Bundle();
        args.putInt("ITEMMENU", itemmenu);
        args.putInt("INDICE", indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.para_lista_listado_items, container, false);

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        reciclador.setLayoutManager(layoutManager);

        int itemmenu = getArguments().getInt("ITEMMENU");
        int indiceSeccion = getArguments().getInt("INDICE");

        switch (itemmenu) {
            case 2: //multimedia
                switch (indiceSeccion) {
                    case 0:
                        adaptadorMultimedia = new ListAdapterMultimedia(DatosMultimedia.FOTOS);
                        break;
                    case 1:
                        adaptadorMultimedia = new ListAdapterMultimedia(DatosMultimedia.VIDEOS);
                        break;
                }
                reciclador.setAdapter(adaptadorMultimedia);
                break;
            /*case 6: //alimentacion
                switch (indiceSeccion) {
                    case 0:
                        adaptadorMultimedia = new AdaptadorCategorias(Comida.PLATILLOS);
                        break;
                    case 1:
                        adaptador6 = new AdaptadorCategorias(Comida.BEBIDAS);
                        break;
                    case 2:
                        adaptador6 = new AdaptadorCategorias(Comida.POSTRES);
                        break;
                }
                reciclador.setAdapter(adaptador6);
                break;*/
        }
        return view;
    }

}
