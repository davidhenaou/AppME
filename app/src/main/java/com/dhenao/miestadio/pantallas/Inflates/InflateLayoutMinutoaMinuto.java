package com.dhenao.miestadio.pantallas.Inflates;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhenao.miestadio.ActividadPrincipal;
import com.dhenao.miestadio.R;
import com.dhenao.miestadio.data.DatosMinutoAMinuto;
import com.dhenao.miestadio.data.DatosMultimedia;
import com.dhenao.miestadio.data.ListAdapterMinutoAMinuto;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
import com.dhenao.miestadio.data.MySql.ConsultaMySql;
import com.dhenao.miestadio.system.Config;

import org.w3c.dom.Text;

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
public class InflateLayoutMinutoaMinuto extends Fragment {

    public int trespttarea;
    private static final int CANTIDAD_ITEMS_CARGA = 10;
    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private ListAdapterMinutoAMinuto adaptadorMinutoaMinuto;
    private SwipeRefreshLayout swipeMinutoaMinuto;
    ConsultaMySql consultaMsql;

    public TextView txtPartidoMensaje;
    public TextView txtPartidoMensaje1;
    public Chronometer cuentapartido;
    public String textoPTST;

    public TextView txtMarcadorEquipo1;
    public TextView txtMarcadorEquipo2;

    public static InflateLayoutMinutoaMinuto nuevaInstancia(int itemmenu,int indiceSeccion) {
        InflateLayoutMinutoaMinuto fragment = new InflateLayoutMinutoaMinuto();
        Bundle args = new Bundle();
        args.putInt("ITEMMENU", itemmenu);
        args.putInt("INDICE", indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenido_minutoaminuto_tab1, container, false);

        reciclador = (RecyclerView) view.findViewById(R.id.recicladorMinutoAMinuto);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        reciclador.setLayoutManager(layoutManager);

        adaptadorMinutoaMinuto = new ListAdapterMinutoAMinuto(Config.MinutoItems);
        reciclador.setAdapter(adaptadorMinutoaMinuto);

         /*obtener fecha y hora del celular*/
        Calendar cal = new GregorianCalendar();
        Date fechamovil = cal.getTime();
        /********************************/

        swipeMinutoaMinuto = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshMinutoAMinuto); //lista de minuto a minuto
        swipeMinutoaMinuto.setColorSchemeResources( R.color.rfs1, R.color.rfs2, R.color.rfs3, R.color.rfs4 );

        ImageButton imagenequipo = (ImageButton) view.findViewById(R.id.imagenequipo1); //imagen de equipo 1
        cuentapartido = (Chronometer) view.findViewById(R.id.cronometropartido);
        txtPartidoMensaje = (TextView) view.findViewById(R.id.txtpartidomensaje);
        txtPartidoMensaje1 = (TextView) view.findViewById(R.id.txtpartidomensaje1);
        cuentapartido.setVisibility(View.INVISIBLE);
        txtPartidoMensaje.setVisibility(View.INVISIBLE);
        txtPartidoMensaje1.setVisibility(View.INVISIBLE);

        TextView txtequipo1 = (TextView) view.findViewById(R.id.nombreequipo1);
        TextView txtequipo2 = (TextView) view.findViewById(R.id.nombreequipo2);

        txtMarcadorEquipo1 = (TextView) view.findViewById(R.id.marcadorequipo1);
        txtMarcadorEquipo2 = (TextView) view.findViewById(R.id.marcadorequipo2);

        txtequipo1.setText(Config.pEquipo1NombreMaM);
        txtequipo2.setText(Config.pEquipo2NombreMaM);

        txtMarcadorEquipo1.setText(Config.marcadorEquipo1);
        txtMarcadorEquipo2.setText(Config.marcadorEquipo2);


        cuentapartido.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
        {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                //cArg.setText(textoPTST +"("+ hh + ":" + mm + ":" + ss + ")");
                cArg.setText(textoPTST +"-"+ mm + ":" + ss );

                txtPartidoMensaje.setText("");
                txtPartidoMensaje1.setText("");

                //Log.d("REVISION INICIAL TIEMPO", (String.valueOf(time / 60)));
                //Log.d("REVISION INICIAL TIEMPO", (String.valueOf(time / 60000)));
                if ((time / 60) < 0) {
                    if (time / 60000 < -180) {
                        txtPartidoMensaje.setVisibility(View.INVISIBLE);
                        txtPartidoMensaje1.setText("Aún falta");
                        txtPartidoMensaje1.setVisibility(View.VISIBLE);
                    } else {
                        if ((time / 60) < -1015) {
                            txtPartidoMensaje.setText("Falta " + (Math.abs(time / 60000) + 1));
                            txtPartidoMensaje.setVisibility(View.VISIBLE);
                            txtPartidoMensaje1.setText("Minutos!");
                            txtPartidoMensaje1.setVisibility(View.VISIBLE);
                        } else {
                            txtPartidoMensaje.setText("Iniciando");
                            txtPartidoMensaje.setVisibility(View.VISIBLE);
                            txtPartidoMensaje1.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    if ((time / 60000) >= 45) {
                        txtPartidoMensaje.setVisibility(View.INVISIBLE);
                        txtPartidoMensaje1.setText("Terminó");
                        txtPartidoMensaje1.setVisibility(View.VISIBLE);
                        cuentapartido.stop();
                    } else {
                        if ((time/60000)%5==0 && s==0){
                            // Get instance of Vibrator from current Context
                            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(300);
                            Toast.makeText(getContext(), "pasaron los minutos", Toast.LENGTH_SHORT).show();
                            swipeMinutoaMinuto.setRefreshing(true);
                            new tareaConsultaMysql().execute();
                        }
                        txtPartidoMensaje.setVisibility(View.INVISIBLE);
                        txtPartidoMensaje1.setText(cArg.getText());
                        txtPartidoMensaje1.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        Log.d("resta", Integer.toString(restarFechas(fechamovil, Config.horapt)));
        if (restarFechas(fechamovil,Config.horapt)>-2700){
            textoPTST = "PT";
            cuentapartido.setBase(SystemClock.elapsedRealtime()+(restarFechas(fechamovil,Config.horapt)*1000));
        }else {
            textoPTST = "ST";
            cuentapartido.setBase(SystemClock.elapsedRealtime() + (restarFechas(fechamovil, Config.horast) * 1000));
        }
        cuentapartido.start();


        imagenequipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Di click en equipo 1", Toast.LENGTH_SHORT).show();
            }
        });


        swipeMinutoaMinuto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override public void onRefresh() {
                //Config.MinutoItems.clear();
                if (txtPartidoMensaje1.getText().equals("Terminó") || txtPartidoMensaje1.getText().equals("Aún falta") || txtPartidoMensaje1.getText().equals("Minutos!")){
                    swipeMinutoaMinuto.setRefreshing(false);
                }else{
                    new tareaConsultaMysql().execute();
                }

            }

        });
        return view;
    }


    public static int restarFechas(Date fechaIn, Date fechaFinal ){
        long in = fechaIn.getTime();
        long fin = fechaFinal.getTime();
        Long diff= (fin-in)/1000;
        return diff.intValue();
    }


    class tareaConsultaMysql extends AsyncTask<String,String,List<ListAdapterMinutoAMinuto>> {


        protected List doInBackground(String... args) {

            adaptadorMinutoaMinuto.clear();
            ConsultaMySql consultaMsql = new ConsultaMySql();
            trespttarea = consultaMsql.consultar(2, getContext());
            consultaMsql.consultar(3, getContext());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Config.MinutoItems;
        }

        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);

           txtMarcadorEquipo1.setText(Config.marcadorEquipo1);
           txtMarcadorEquipo2.setText(Config.marcadorEquipo2);

           //if (trespttarea==1) adaptadorMinutoaMinuto.clear();

           // adaptadorMinutoaMinuto = new ListAdapterMinutoAMinuto(Config.MinutoItems);
            // Añadir elementos nuevos
            adaptadorMinutoaMinuto.addAll( Config.MinutoItems);
            adaptadorMinutoaMinuto.notifyDataSetChanged();


            // Parar la animación del indicador
            swipeMinutoaMinuto.setRefreshing(false);

            reciclador.getAdapter().notifyItemRangeInserted(0, 0);

            //reciclador.setAdapter(adaptadorMinutoaMinuto);
        }



    }







}
