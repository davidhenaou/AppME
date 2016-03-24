package com.dhenao.miestadio;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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

import com.dhenao.miestadio.data.JSONParser;
import com.dhenao.miestadio.data.ListAdapterMultimedia;
import com.dhenao.miestadio.ui.CargaContenido;
import com.dhenao.miestadio.ui.CargarContenidoViewPager;
import com.dhenao.miestadio.ui.MultiTouchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import com.dhenao.miestadio.system.sync.Autenticacion;

public class ActividadPrincipal extends AppCompatActivity {
    private DrawerLayout drawerLayout; //del menu principal

    /***para la carga de imagenes***/
    //public static final String URL = "http://www.thebiblescholar.com/android_awesome.jpg";
    //de esta pagina por hacer - https://sekthdroid.wordpress.com/2012/12/01/guardar-imagen-en-memoria-interna-android/

    /***para la multimedia, toma de fotos**/
    private final String ruta_temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";
    private File filefotos = new File(ruta_temp);

    /*** para el refresco de listas*/
    private RecyclerView recycler;
    private ListAdapterMultimedia adapter;
    private RecyclerView.LayoutManager lManager;
    private SwipeRefreshLayout refreshLayout;

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

    // equipos JSONArray
    JSONArray equipos = null;
    public ListView lista;



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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config0, menu);
        return true;
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

/* para el menu contextual.... no borrar
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_multimediacontext, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.mult_tomarfoto:
                Toast.makeText(getApplicationContext(), "selecciono tomar foto" , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mult_subirfoto:
                Toast.makeText(getApplicationContext(), "selecciono subir foto" , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mult_cancelarfoto:
                Toast.makeText(getApplicationContext(), "selecciono cancelar" , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
*/


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
            case R.id.nav_3:
                String username = "david";
                String password = "324126";

                Account account = new Account(username, getString(R.string.ACCOUNT_TYPE));
                AccountManager am = AccountManager.get(this);
                boolean accountCreated = am.addAccountExplicitly(account, password, null);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    if (accountCreated) {  //Pass the new account back to the account manager
                        AccountAuthenticatorResponse response = extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
                        Bundle result = new Bundle();
                        result.putString(AccountManager.KEY_ACCOUNT_NAME, username);
                        result.putString(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.ACCOUNT_TYPE));
                        response.onResult(result);
                    }
                    finish();
                }


                break;
            case R.id.nav_4:
                //Intent in = new Intent(this, Autenticacion.class );
                //startActivity(in);


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

            case R.id.accion_camara:
                //Si no existe crea la carpeta donde se guardaran las fotos
                filefotos.mkdirs();
                File mi_foto = new File( ruta_temp + "miest" +  tomarFechayhora() + ".jpg" );
                try {
                    mi_foto.createNewFile();
                } catch (IOException ex) {
                    Log.e("ERROR ", "Error:" + ex);
                }
                //
                Uri uri = Uri.fromFile( mi_foto );
                //Abre la camara para tomar la foto
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Guarda imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //Retorna a la actividad
                startActivityForResult(cameraIntent, 0);

                /*aqui debo coger la foto de la ruta y subirla*/

                return true;
            case R.id.menuconf_configuracion:
                //startActivity(new Intent(this, ActividadConfiguracion.class));
                //return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void clickEnImagen(View target) {
        switch (target.getId()) {
            case R.id.imagenequipo1:
                ImageView imagene = (ImageView) findViewById(R.id.imagenequipo1);
                registerForContextMenu(imagene);

                Toast.makeText(getApplicationContext(), "selecciono la imagen del equipo1" , Toast.LENGTH_SHORT).show();
                ImageView imagen = (ImageView) findViewById(R.id.imagenresultante);
                imagen.setBackground(target.getBackground());
                break;

            case R.id.imagenequipo2:
                Toast.makeText(getApplicationContext(), "selecciono la imagen del equipo2" , Toast.LENGTH_SHORT).show();
                /*ImageView*/ imagen = (ImageView) findViewById(R.id.imagenresultante);
                imagen.setBackground(target.getBackground());
                break;

            case R.id.imagenminiatura:
                /*Intent i = new Intent(ActividadPrincipal.this, MultiTouchActivity.class );
                i.putExtra("rutaimagen", target);
                startActivity(i); */

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

    //LO USO CUANDO NECESITO UN STRING DE FECHA Y HORA
    @SuppressLint("SimpleDateFormat")
    private String tomarFechayhora()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddmmyyyyhhmmss");
        return dateFormat.format(new Date());
    }

}
