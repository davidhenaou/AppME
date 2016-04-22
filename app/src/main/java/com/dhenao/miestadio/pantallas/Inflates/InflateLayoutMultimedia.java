package com.dhenao.miestadio.pantallas.Inflates;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.DatosMultimedia;
import com.dhenao.miestadio.data.ListAdapterMinutoAMinuto;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
import com.dhenao.miestadio.data.MySql.ConsultaMySql;
import com.dhenao.miestadio.data.SQlite.DatabaseHandler;
import com.dhenao.miestadio.data.SQlite.EquipoFutbol;
import com.dhenao.miestadio.data.SQlite.tbConfiguracion;
import com.dhenao.miestadio.system.Config;
import com.dhenao.miestadio.system.Herramientas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
//import com.dhenao.miestadio.data.DatosMultimedia;

/**
 * Llenado de las pestañas
 */
public class InflateLayoutMultimedia extends Fragment {

    public int trespt = 0;
    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private ListAdapterMultimedia adaptadorMultimedia;
    public static final List<DatosMultimedia> FOTOS = new ArrayList<DatosMultimedia>();
    DatabaseHandler db;
    public Date horaRefresco;
    Herramientas herramientas;
    private SwipeRefreshLayout swipeMultimedia;

    public static InflateLayoutMultimedia nuevaInstancia(int itemmenu,int indiceSeccion) {
        InflateLayoutMultimedia fragment = new InflateLayoutMultimedia();
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

        swipeMultimedia = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh); //lista de minuto a minuto
        swipeMultimedia.setColorSchemeResources(R.color.rfs1, R.color.rfs2, R.color.rfs3, R.color.rfs4);

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
        }

        swipeMultimedia.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                ejecutaTareaMultimedia();
                /*
                Calendar cal = new GregorianCalendar();
                Date horaRefresco1 = cal.getTime();
                int SegundosPasados = restarFechas(horaRefresco, horaRefresco1);

                if (txtPartidoMensaje1.getText().equals("Terminó") || txtPartidoMensaje1.getText().equals("Aún falta") || txtPartidoMensaje1.getText().equals("Minutos!") || SegundosPasados<60){
                    swipeMinutoaMinuto.setRefreshing(false);
                }else{
                    new tareaConsultaMysql().execute();
                }*/

            }

        });

        return view;
    }


    public void ejecutaTareaMultimedia(){
        swipeMultimedia.setRefreshing(true);
        Calendar cal = new GregorianCalendar();
        Date horaRefresco1 = cal.getTime();
        int SegundosPasados = herramientas.restarFechas(horaRefresco, horaRefresco1);

        if (SegundosPasados<60){
            swipeMultimedia.setRefreshing(false);
        }else{
            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            new TareaConsultaMultimedia().execute();
        }
    }

    class TareaConsultaMultimedia extends AsyncTask<String,String,List<ListAdapterMultimedia>> {


        protected List doInBackground(String... args) {

            Calendar cal = new GregorianCalendar();
            horaRefresco =  cal.getTime();

            //if (Config.conexionSistema) adaptadorMultimedia.clear();
            ConsultaMySql consultaMsql = new ConsultaMySql();
            consultaMsql.consultar(4, getContext(),Config.idmultimedia);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Config.MultimediaItems;
        }

        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);
/*
            txtMarcadorEquipo1.setText(Config.marcadorEquipo1);
            txtMarcadorEquipo2.setText(Config.marcadorEquipo2);

            // Añadir elementos nuevos
            adaptadorMinutoaMinuto.addAll( Config.MinutoItems);
            adaptadorMinutoaMinuto.notifyDataSetChanged();
*/
            // Parar la animación del indicador
            swipeMultimedia.setRefreshing(false);
  //          reciclador.getAdapter().notifyItemRangeInserted(0, 0);

        }

    }

}
