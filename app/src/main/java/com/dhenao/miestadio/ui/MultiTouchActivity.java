package com.dhenao.miestadio.ui;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;

public class MultiTouchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle parametros = this.getIntent().getExtras();
        Bitmap b = BitmapFactory.decodeByteArray(parametros.getByteArray("imagen"), 0, parametros.getByteArray("imagen").length);

        TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(b);
        //img.setBackground(b);
        //img.setImageResource(R.drawable.foto1);
        img.setMaxZoom(4f);
        setContentView(img);
    }
}
