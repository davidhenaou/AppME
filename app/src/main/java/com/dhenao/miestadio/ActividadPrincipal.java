package com.dhenao.miestadio;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhenao.miestadio.data.DatosMultimedia;
import com.dhenao.miestadio.data.JSONParser;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
import com.dhenao.miestadio.ui.CargaContenido;
import com.dhenao.miestadio.ui.CargarContenidoViewPager;
import com.dhenao.miestadio.ui.MultiTouchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActividadPrincipal extends AppCompatActivity {
    private DrawerLayout drawerLayout; //del menu principal

    /*** para el refresco de listas*/
    private RecyclerView recycler;
    private ListAdapterMultimedia adapter;
    private RecyclerView.LayoutManager lManager;
    private SwipeRefreshLayout refreshLayout;
    private static final int CANTIDAD_ITEMS = 8;

    /*otros para pruebas*/
    public String tituloPerfil;

    /*para el webservice*/
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> empresaList;
    // url to get all euiqpos list
    private static String url_info_equipos = "http://losurreas.eshost.com.ar/app/obtener_equipos.php";
    // JSON Node names
    private static final String TAG_ESTADO = "estado";
    private static final String TAG_EQUIPOS = "equipos";
    private static final String TAG_TITULO = "titulo";
    private static final String TAG_DETALLE = "detalle";

    ImageView btn[] = new ImageView[2];

    // equipos JSONArray
    JSONArray equipos = null;
    ListView lista;

    public void clickEnImagen(View target) {
        switch (target.getId()) {
            case R.id.imagenequipo1:
                Toast.makeText(getApplicationContext(), "selecciono la imagen del equipo1" , Toast.LENGTH_SHORT).show();
                ImageView imagen = (ImageView) findViewById(R.id.imagenresultante);
                imagen.setBackground(target.getBackground());
                break;

            case R.id.imagenequipo2:
                Toast.makeText(getApplicationContext(), "selecciono la imagen del equipo2" , Toast.LENGTH_SHORT).show();
                break;

            case R.id.imagenminiatura:
                /*Intent i = new Intent(ActividadPrincipal.this, MultiTouchActivity.class );
                i.putExtra("rutaimagen", target);
                startActivity(i); */

               //startActivity(new Intent(ActividadPrincipal.this, MultiTouchActivity.class).putExtras(new Bundle().putParcelable("bitmap", target.getDrawingCache())));

                Intent i = new Intent(ActividadPrincipal.this, MultiTouchActivity.class );
                target.setDrawingCacheEnabled(true);
                Bitmap imagenpasada = target.getDrawingCache();
                Bundle b = new Bundle(); //Creo un contenedor Bundle
                ByteArrayOutputStream bs = new ByteArrayOutputStream(); //Creo un ByteArray y convierto el Bitmap a este formato
                imagenpasada.compress(Bitmap.CompressFormat.PNG, 50, bs);
                b.putByteArray("imagen", bs.toByteArray()); //Pongo el ByteArray en el Bundle con el id "nombre"
                i.putExtras(b); //y lo envío cuando pulse el botón
                startActivity(i);

                break;
        }
        //reciclador = (RecyclerView) findViewById(R.id.reciclador);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("inicio app", "Start");

        setContentView(R.layout.menu_deslizante_y_contenido);
        agregarToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); //menu deslizante
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        TableLayout datosPerfil = (TableLayout) findViewById(R.id.datos_perfil);
        TextView tituloperfil = (TextView) findViewById(R.id.titulo_perfil);
        tituloPerfil = tituloperfil.getText().toString();

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto el que inicia
            seleccionarItem(navigationView.getMenu().getItem(0));
        }





        /*NO BORRAR funciona... configuracion del boton flotante*/
        /*FloatingActionButton botonflotante = (FloatingActionButton) findViewById(R.id.botonflotante);
        botonflotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private boolean verificaConexion(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
             Toast.makeText(this, "con conexion", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "sin conexion", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config0, menu);
        return true;
    }


    private void seleccionarItem(MenuItem itemDrawer) {

        /*funciona... cargar ventana externa
                i = new Intent(this, CargaralgunContenido.class );
        startActivity(i);
        */
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) { //FragmentoMultimedia();
            case R.id.nav_1:
                fragmentoGenerico = CargarContenidoViewPager.nuevaInstancia(1, 1);
                break;
            case R.id.nav_2:
                fragmentoGenerico = CargarContenidoViewPager.nuevaInstancia(2, 4);
                break;
            case R.id.nav_5:
                new CargarLosEquipos().execute();
                break;
            case R.id.nav_6:
                boolean conex = verificaConexion();
                if (conex){
                    fragmentoGenerico = CargarContenidoViewPager.nuevaInstancia(2, 4);
                }
                break;
            case R.id.nav_7:
                Intent i = new Intent(this, CargaContenido.class );
                startActivity(i);
                break;
            case R.id.nav_8:
                finish();
                break;
        }
        //fragmentoGenerico = FragmentoPestanas.nuevaInstancia(1,1);

        if (fragmentoGenerico != null) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_principal, fragmentoGenerico).commit();
        }
        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // recuerde si mete actividades el AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menuconf_configuracion:
                //startActivity(new Intent(this, ActividadConfiguracion.class));
                //return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class HackingBackgroundTask extends AsyncTask<Void, Void, List<DatosMultimedia>> {

        static final int DURACION = 3 * 1000; // 3 segundos de carga

        @Override
        protected List<DatosMultimedia> doInBackground(Void... params) {
            // Simulación de la carga de items
            try {
                Thread.sleep(DURACION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Retornar en nuevos elementos para el adaptador
            return DatosMultimedia.randomList(CANTIDAD_ITEMS);
        }

        @Override
        protected void onPostExecute(List<DatosMultimedia> result) {
            super.onPostExecute(result);

            // Limpiar elementos antiguos
            adapter.clear();

            // Añadir elementos nuevos
            adapter.addAll(result);

            // Parar la animación del indicador
            refreshLayout.setRefreshing(false);
        }

    }






    class CargarLosEquipos extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActividadPrincipal.this);
            pDialog.setMessage("Haciendo la consulta. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo los equipos
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();

            Toast.makeText(getApplicationContext(), "jParser.makeHttpRequest", Toast.LENGTH_SHORT).show();
            // getting JSON string from URL
            Toast.makeText(getApplicationContext(), "antes de json" , Toast.LENGTH_SHORT).show();
            JSONObject json = jParser.makeHttpRequest(url_info_equipos, "GET", params);
            Toast.makeText(getApplicationContext(), "despues de json" , Toast.LENGTH_SHORT).show();

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_ESTADO);

                if (success == 1) {
                    // titulos found
                    // Getting Array of Products
                    equipos = json.getJSONArray(TAG_TITULO);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + equipos.length());
                    for (int i = 0; i < equipos.length(); i++) {
                        JSONObject c = equipos.getJSONObject(i);
/*
                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NOMBRE);

                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NOMBRE, name);

                        empresaList.add(map);
  */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
       /*
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all equipos
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            ActividadPrincipal.this,
                            empresaList,
                            R.layout.contenido_item1,
                            new String[]{
                                    TAG_ID,
                                    TAG_NOMBRE,
                            },
                            new int[]{
                                    R.id.single_post_tv_id,
                                    R.id.single_post_tv_nombre,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);
                }
            });
        }*/
    }

}
