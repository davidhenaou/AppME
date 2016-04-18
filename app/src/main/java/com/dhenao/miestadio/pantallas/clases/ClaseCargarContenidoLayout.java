package com.dhenao.miestadio.pantallas.clases;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhenao.miestadio.R;

public class ClaseCargarContenidoLayout extends Fragment{
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    View vistaContenidoLayaout;

    public static ClaseCargarContenidoLayout nuevaInstancia(int itemMenu,int compMenu ) {
        ClaseCargarContenidoLayout fragment = new ClaseCargarContenidoLayout();
        Bundle args = new Bundle();
        args.putInt("itemMenu", itemMenu);
        args.putInt("compMenu", compMenu);
        fragment.setArguments(args);
        return fragment;
    }

    // en caso de que se cree un ViewGroup
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        int itemMenu = getArguments().getInt("itemMenu");
        switch (itemMenu) {
            case 3: //narracion del partido
                vistaContenidoLayaout = inflater.inflate(R.layout.contenido_narracion, container, false);
                break;

            case 5: //Calendario
                vistaContenidoLayaout = inflater.inflate(R.layout.contenido_calendario, container, false);
                break;
        }

        if (savedInstanceState == null) {
            View padre = (View) container.getParent();
            //appBarLayout = (AppBarLayout) padre.findViewById(R.id.barraapp); //el padre es layout contenido_navegacion_conbarra
            tabLayout = new TabLayout(getActivity());
            tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            //appBarLayout.addView(tabLayout);

        }
        return vistaContenidoLayaout;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int compMenu = getArguments().getInt("compMenu");
        if (compMenu>=100) {
            inflater.inflate(R.menu.menu_config100, menu);
            compMenu = compMenu - 100;
        }
        if (compMenu==1 || compMenu==3 || compMenu==5 || compMenu==7){
            inflater.inflate(R.menu.menu_buscar1, menu);
        }
        if (compMenu==2 || compMenu==3 || compMenu==6 || compMenu==7) {
            inflater.inflate(R.menu.menu_compras2, menu);
        }
        if (compMenu==4 || compMenu==5 || compMenu==6 || compMenu==7) {
            inflater.inflate(R.menu.menu_multimedia4, menu);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //appBarLayout.removeView(tabLayout);
    }
}
