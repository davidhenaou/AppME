package com.dhenao.miestadio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhenao.miestadio.data.MySql.RefrescarObjetos;
import com.dhenao.miestadio.pantallas.MinutoaMinuto;
import com.dhenao.miestadio.system.ActividadConfiguracion;
import com.dhenao.miestadio.system.Config;
import com.dhenao.miestadio.system.autenticacion.LogueoActivity;
import com.dhenao.miestadio.pantallas.SubirMultimediaaSevidor;
import com.dhenao.miestadio.ui.CargaContenido;
import com.dhenao.miestadio.ui.CargarContenidoViewPager;
import com.dhenao.miestadio.ui.MultiTouchActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActividadPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout; //del menu principal

    /***para la carga de imagenes***/
    private static final String TAG = ActividadPrincipal.class.getSimpleName();
    private static final int CAPTURA_CAMARA_IMAGEN_CODIGO_RESPUESTA = 100;
    private static final int CAPTURA_BIBLIOTECA_IMAGEN_CODIGO_RESPUESTA = 101;
    private static final int CAPTURA_CAMARA_VIDEO_CODIGO_RESPUESTA = 200;
    private static final int CAPTURA_BIBLIOTECA_VIDEO_CODIGO_RESPUESTA = 201;
    private static final int CAPTURA_IMAGEN_PERFIL_CODIGO_RESPUESTA = 110;
    private static final int RECORTA_IMAGEN_CODIGO_RESPUESTA = 300;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri; // file url to store image/video

    /***para la multimedia, toma de fotos**/
    public final String ruta_temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";
    public File rutafotos = new File(ruta_temp);
    public File fileFotoCaptura;
    public Uri uriCaptura;

    /*para los cuadros de dialogo*/
    private ProgressDialog pDialog;

    RefrescarObjetos refrecoObjetos;

    /*** para el refresco de listas*/
    /*private RecyclerView recycler;
    private ListAdapterMultimedia adapter;
    private RecyclerView.LayoutManager lManager;
    private SwipeRefreshLayout refreshLayout;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("inicio app", "Start");
        refrecoObjetos = new RefrescarObjetos();

        setContentView(R.layout.menu_deslizante_y_contenido);
        agregarToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); //menu deslizante
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view); //navegador
        ImageView imagenPerfil = (ImageView) findViewById(R.id.icono_miperfil); //imagen de perfil

        if (navigationView != null) {
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0)); // Seleccionar item por defecto el que inicia
            refrecoObjetos.CargaPerfil(ActividadPrincipal.this); //CargarPerfil();
        }

        if ( TextUtils.isEmpty(Config.UsuarioPerfil) || TextUtils.isEmpty(Config.CorreoPerfil) || TextUtils.isEmpty(Config.CelularPerfil)) {
            Intent intent = new Intent(this, LogueoActivity.class);
            startActivityForResult(intent, 1234);
        }

        //capturar imagen de perfil
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                captureMultimedia(5);
                /*rutafotos.mkdirs();
                fileFotoCaptura = new File(ruta_temp + "miestadioperfil.jpg");
                try { fileFotoCaptura.createNewFile(); } catch (IOException ex) { Log.e("ERROR ", "Error:" + ex); }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileFotoCaptura));
                startActivityForResult(cameraIntent, CAPTURA_IMAGEN_PERFIL_CODIGO_RESPUESTA);*/
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_basico0, menu);
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


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case CAPTURA_CAMARA_IMAGEN_CODIGO_RESPUESTA: //si viene de la captura de foto para multimedia
                if(resultCode == RESULT_OK){
                    lanzarSubirImagenWeb(true);
                }else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "El usuario ha cancelado la captura", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Lo siento, fallo en la captura", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAPTURA_BIBLIOTECA_IMAGEN_CODIGO_RESPUESTA: //si viene de la seleccion de imagen para multimedia
                if(resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = this.managedQuery(fileUri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    fileUri = Uri.parse(cursor.getString(column_index));
                    fileFotoCaptura = new File( fileUri.toString() );
                    Bitmap imagentemp = rotarImagen(fileFotoCaptura);
                    File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                    try {
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        imagentemp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                    }  catch (Exception e) {
                        Log.e("ERROR ", "Error:" + e);
                    }
                    fileUri = Uri.parse("file://" + file.toString());
                    lanzarSubirImagenWeb(true);
                }else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Cancelada la operacion", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Lo siento, fallo en la operacion", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAPTURA_CAMARA_VIDEO_CODIGO_RESPUESTA:
                if (resultCode == RESULT_OK) {
                    lanzarSubirImagenWeb(false);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"El usuario ha cancelado la captura", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Lo siento, fallo en la captura", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAPTURA_BIBLIOTECA_VIDEO_CODIGO_RESPUESTA:
                if (resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    //String tempPath = getPath(fileUri, ActividadPrincipal.this);
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = this.managedQuery(fileUri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    fileUri = Uri.parse(cursor.getString(column_index));
                    lanzarSubirImagenWeb(false);
                }else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Cancelada la operacion", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Lo siento, fallo en la operacion", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAPTURA_IMAGEN_PERFIL_CODIGO_RESPUESTA: //capturar imagen de perfil
                if(resultCode == RESULT_OK) {
                    Bitmap imagentemp = rotarImagen(fileFotoCaptura);
                    String rutaimagen = guardarImagenRutaApp(getApplicationContext(), "imagenperfil", imagentemp);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("com.dhenao.miestadio_preferences", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("ImagenPerf", rutaimagen);
                    editor.commit();
                    refrecoObjetos.CargaPerfil(ActividadPrincipal.this); //CargarPerfil();
                }
                break;

            /*case RECORTA_IMAGEN_CODIGO_RESPUESTA: //recorto la imagen
                Bundle extras = data.getExtras();
                Bitmap imagenRecortada = extras.getParcelable("data");
                fileFotoCaptura = new File(ruta_temp + "miestadioperfil.jpg");
                //fileFotoCaptura.createNewFile();

                String rutaimagen = guardarImagenRutaApp(getApplicationContext(), "imagenperfil", imagenRecortada);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("com.dhenao.miestadio_preferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("ImagenPerf", rutaimagen);
                editor.commit();
                refrecoObjetos.CargaPerfil(ActividadPrincipal.this); //CargarPerfil();
                break;*/

            case 1234: //si viene del logueo
                if (resultCode == RESULT_CANCELED){
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle("Salir de Mi Estadio");
                    dialogo1.setMessage("¿ Desea salir de Mi Estadio ?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            finish();
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Intent intent = new Intent(getApplicationContext(), LogueoActivity.class);
                            startActivityForResult(intent, 1234);
                        }
                    });
                    dialogo1.show();
                }
                refrecoObjetos.CargaPerfil(ActividadPrincipal.this); //CargarPerfil();
                break;
        }
    }


    public Bitmap rotarImagen(File fileRotar) {
        Bitmap imagentemp = BitmapFactory.decodeFile(fileRotar.toString());
        try {
            ExifInterface exif = new ExifInterface(fileRotar.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            imagentemp = Bitmap.createBitmap(imagentemp, 0, 0, imagentemp.getWidth(), imagentemp.getHeight(), matrix, true);
        } catch (Exception e){
            Log.e("ERROR ", "Error:" + e);
        }
        return imagentemp;
    }


    private void lanzarSubirImagenWeb(boolean isImage){
        Intent i = new Intent(ActividadPrincipal.this, SubirMultimediaaSevidor.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
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
                fragmentoGenerico = CargarContenidoViewPager.nuevaInstancia(1, 101);
                break;
            case R.id.nav_2:
                fragmentoGenerico = CargarContenidoViewPager.nuevaInstancia(2, 4);
                break;
            case R.id.nav_3:

                break;
            case R.id.nav_4:

                break;
            case R.id.nav_5:

                break;
            case R.id.nav_6:

                break;
            case R.id.nav_7:
                Intent i = new Intent(this, CargaContenido.class );
                startActivity(i);
                break;
            case R.id.nav_8:
                finish();
                break;
        }

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

            case R.id.mult_tomarfoto:
                captureMultimedia(1);
                return true;

            case R.id.mult_subirfoto:
                captureMultimedia(2);
                return true;

            case R.id.mult_tomarvideo:
                captureMultimedia(3);
                return true;

            case R.id.mult_subirvideo:
                captureMultimedia(4);
                return true;

            case R.id.menuconf_configuracion:
                startActivity(new Intent(this, ActividadConfiguracion.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void captureMultimedia(int tipo) {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent;
            switch (tipo) {
                case 1: //capturo foto
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAPTURA_CAMARA_IMAGEN_CODIGO_RESPUESTA);
                    break;

                case 2: //seleccion de foto de biblioteca
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Con que abrira la imagen?"), CAPTURA_BIBLIOTECA_IMAGEN_CODIGO_RESPUESTA);
                    break;

                case 3: //capturo video
                    intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                    startActivityForResult(intent, CAPTURA_CAMARA_VIDEO_CODIGO_RESPUESTA);
                    break;

                case 4: //seleccionar un video
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent.createChooser(intent, "Con que abrira el video?"), CAPTURA_BIBLIOTECA_VIDEO_CODIGO_RESPUESTA);
                    break;

                case 5: //capturar imagen de perfil
                    rutafotos.mkdirs();
                    fileFotoCaptura = new File(ruta_temp + "miestadioperfil.jpg");
                    try { fileFotoCaptura.createNewFile(); } catch (IOException ex) { Log.e("ERROR ", "Error:" + ex); }
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileFotoCaptura));
                    startActivityForResult(cameraIntent, CAPTURA_IMAGEN_PERFIL_CODIGO_RESPUESTA);
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Tu camara no esta lista!!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri); // save file url in bundle as it will be null on screen orientation changes
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri"); // get the file url
    }



    private String guardarImagenRutaApp (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre + ".jpg");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
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



    //LO USO CUANDO NECESITO UN STRING DE FECHA Y HORA
    @SuppressLint("SimpleDateFormat")
    private String tomarFechayhora()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddmmyyyyhhmmss");
        return dateFormat.format(new Date());
    }





    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME_LOCAL);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME_LOCAL + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    /*todas las llamadas a otras ventanas de contenido*/

    public void clickMinutoAminuto(View target) { //para cargar la ventana de minuto a minuto
        Intent i = new Intent(ActividadPrincipal.this, MinutoaMinuto.class );
        startActivity(i);
    }






    /*tarea que hace la consulta a mysql*/
    /*
    class TareaAsincronicaMySql extends AsyncTask<String,String,String> {
        String mensajeProgress;
        ConsultaMySql consultaMsql;
        RefrescarObjetos refrecoObjetos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pQuemoduloConsulta==1) mensajeProgress="Cargando Informacion de Minuto A Minuto";

            pDialog = new ProgressDialog(ActividadPrincipal.this);
            pDialog.setMessage(mensajeProgress + ", Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if(pConsultarMySql) pDialog.show();
        }

        protected String doInBackground(String... args) {
            consultaMsql = new ConsultaMySql(pQuemoduloConsulta,pConsultarMySql);
            return null;
        }

        protected void onPostExecute(String file_url) {
            //desaparezco el cuadro de dialogo
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    refrecoObjetos = new RefrescarObjetos(pResfreconInstantaneo, pQuemoduloConsulta, ActividadPrincipal.this);
                    //consultaMsql.ResfrescarInformacion(pResfreconInstantaneo, pQuemoduloConsulta, ActividadPrincipal.this);
                }
            });
        }
    }*/



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


    /*
    public void CargarPerfil() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ActividadPrincipal.this);
        Config.UsuarioPerfil = pref.getString("UsuarioPref", "").trim();
        Config.CorreoPerfil = pref.getString("CorreoPref", "").trim();
        Config.CelularPerfil = pref.getString("CelularPref", "").trim();
        String rutaImagenperf = pref.getString("ImagenPerf", "").trim();

        TextView edtUsuarioPerfil = (TextView) findViewById(R.id.perfil_usuario);
        TextView edtCorreoPerfil = (TextView) findViewById(R.id.perfil_correo);
        TextView edtCelularPerfil = (TextView) findViewById(R.id.perfil_celular);
        ImageView imagenPerfil = (ImageView) findViewById(R.id.icono_miperfil);

        edtUsuarioPerfil.setText(Config.UsuarioPerfil);
        edtCorreoPerfil.setText(Config.CorreoPerfil);
        edtCelularPerfil.setText(Config.CelularPerfil);
        if (!TextUtils.isEmpty(rutaImagenperf)){
            File imgFile = new File(rutaImagenperf);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imagenPerfil.setImageBitmap(myBitmap);
        }
    }
    */

}



