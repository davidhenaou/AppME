package com.dhenao.miestadio.pantallas.clases;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.pantallas.Inflates.InflateLayoutCalendario;
import com.dhenao.miestadio.pantallas.Inflates.InflateLayoutMinutoaMinuto;
import com.dhenao.miestadio.pantallas.Inflates.InflateLayoutNarracion;

public class ClaseCargarContenidoViewPager extends Fragment{
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static ClaseCargarContenidoViewPager nuevaInstancia(int itemMenu,int compMenu ) {
        ClaseCargarContenidoViewPager fragment = new ClaseCargarContenidoViewPager();
        Bundle args = new Bundle();
        args.putInt("itemMenu", itemMenu);
        args.putInt("compMenu", compMenu);
        fragment.setArguments(args);
        return fragment;
    }

    // en caso de que se cree un ViewGroup
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewContenido_Paginado = inflater.inflate(R.layout.contenido_paginado, container, false);
        int itemMenu = getArguments().getInt("itemMenu");
        //int compMenu = getArguments().getInt("compMenu");
        if (savedInstanceState == null) {
            View padre = (View) container.getParent();
            appBarLayout = (AppBarLayout) padre.findViewById(R.id.barraapp); //el padre es layout contenido_navegacion_conbarra
            tabLayout = new TabLayout(getActivity());
            tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            appBarLayout.addView(tabLayout);

            // Setear adaptador al viewpager.
            viewPager = (ViewPager) viewContenido_Paginado.findViewById(R.id.pager); //el layout contenido_paginado
            titulosViewPager(viewPager, itemMenu);
            tabLayout.setupWithViewPager(viewPager);
        }
        return viewContenido_Paginado;
    }


    private void titulosViewPager(ViewPager viewPager, int itemMenu) {
        GestionTabs adapter = new GestionTabs(getFragmentManager());

        int nmeroItem = 0;
        switch (itemMenu) {
            case 1: //Partido (item del menu, numero de item)
                adapter.addFragment(InflateLayoutMinutoaMinuto.nuevaInstancia(itemMenu, nmeroItem++),getString(R.string.titulo_item1_tab1));
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item1_tab2));
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item1_tab3));
                break;
            case 2: //multimedia (item del menu, numero de item)
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item2_tab1));
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item2_tab2));
                break;

            case 3: //Narracion
                adapter.addFragment(InflateLayoutNarracion.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.menuitem_3));
                break;



            case 5: //Partido (item del menu, numero de item)
                adapter.addFragment(InflateLayoutCalendario.nuevaInstancia(itemMenu, nmeroItem++),"Liga Postobon");
                adapter.addFragment(InflateLayoutCalendario.nuevaInstancia(itemMenu, nmeroItem++), "Liga Aguila");
                break;


            case 6:
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item6_tab1));
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item6_tab2));
                adapter.addFragment(ClaseTabsLLenadoConListas.nuevaInstancia(itemMenu, nmeroItem++), getString(R.string.titulo_item6_tab3));
                break;
        }
        viewPager.setAdapter(adapter);
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
        appBarLayout.removeView(tabLayout);
    }
}
