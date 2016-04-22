package com.dhenao.miestadio.pantallas;

import com.dhenao.miestadio.R;
import com.dhenao.miestadio.system.Config;
import com.dhenao.miestadio.system.sync.AndroidMultiPartEntity;
import com.dhenao.miestadio.system.sync.AndroidMultiPartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dhenao.miestadio.ActividadPrincipal;

public class SubirMultimediaaSevidor extends Activity {
    private static final String TAG = ActividadPrincipal.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    //private TextView txtPercentage;
    private TextView txtComentarios;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;
    private File file;
    boolean isImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subir_multimedia_a_servidor);
        //txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        txtComentarios = (TextView) findViewById(R.id.edtcomentarios);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);

        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            previewMedia();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Lo siento, ruta de archivo perdida!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = new File(filePath);
                if (isImage) {
                    if (file.length() <= 10000000) //permite imagenes menores a 10 megas
                    {
                        new UploadFileToServer().execute();
                    } else {
                        showAlert("El tamaño maximo de las imagenes es de 10 Megas, Intente con otra foto");
                    }
                }else{
                    if (file.length() <= 40000000) //permite videos menores a 30 megas
                    {
                        new UploadFileToServer().execute();
                    } else {
                        showAlert("El tamaño maximo de los videos es de 40 Megas, Intente con otro video");
                    }
                }
            }
        });
    }


    private void previewMedia() {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            vidPreview.start();
        }
    }



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);
            // updating percentage value
            btnUpload.setText("Cargando (" + String.valueOf(progress[0]) + "%)");
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

                Log.i("SubidaApp", "url de subida: " + Config.FILE_UPLOAD_URL);


                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("usuario", new StringBody(Config.UsuarioPerfil));
                entity.addPart("correo", new StringBody(Config.CorreoPerfil));
                entity.addPart("celular", new StringBody(Config.CelularPerfil));
                entity.addPart("comentarios", new StringBody(txtComentarios.getText().toString()));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = "Se ha subido el archivo correctamente, gracias " + Config.UsuarioPerfil + " por compartirlo.";
                    //responseString = EntityUtils.toString(r_entity);
                } else {
                    Log.d("Error statusCode",Integer.toString(statusCode));
                    Log.d("Error devuelto response",response.toString());
                    Log.d("Error devuelto r_entity",r_entity.toString());
                    responseString = "Ha ocurrido un error. Por favor intente de nuevo.";
                }

            } catch (ClientProtocolException e) {
                responseString = "Ha ocurrido un error. Por favor intente de nuevo.";
            } catch (IOException e) {
                responseString = "Problemas de conexión con el servidor. No hay transferencia de datos."; //e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            /*String Resultadopos;
            try {
                JSONObject obj = new JSONObject(result);
                Resultadopos = obj.get("usuario").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            showAlert(result);
            super.onPostExecute(result);
        }

    }


    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Archivo Multimedia")
                .setCancelable(false)
                .setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}